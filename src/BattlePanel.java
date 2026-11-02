import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;

/** หน้าจอต่อสู้ ควบคุมลำดับ: แสดงโจทย์ > รับคำตอบ > เล่นท่าโจมตี > ข้อถัดไป */
public class BattlePanel extends JPanel implements Updatable, CountdownThread.Listener {

    private enum State { ANSWERING, ANIMATING, FINISHED }

    private static final int NORMAL_HIT_FRAME = GameCharacter.MELEE_HIT_FRAME;
    private static final int SPECIAL_HIT_FRAME = GameCharacter.CAST_FRAMES;
    private static final int CORRECT_PAUSE_FRAMES = 50;
    private static final int WRONG_PAUSE_FRAMES = 100; // นานกว่าเพื่อให้อ่านเฉลยทัน
    private static final int DEATH_PAUSE_FRAMES = 30;

    private final GameFrame frame;
    private final QuestionPanel questionPanel = new QuestionPanel();
    private final GameButton attackButton =
        new GameButton("Attack", GameConfig.RED, GameConfig.DARK, 22);
    private final List<Effect> effects = new ArrayList<>();

    private Level level;
    private Player player;
    private Enemy enemy;
    private Obstacle obstacle;
    private Battle battle;
    private Question question;
    private GameLoop gameLoop;
    private CountdownThread countdown;

    private State state = State.FINISHED;
    private Attack pendingAttack;
    private int animationFrame;
    private int hitFrame;
    private int endFrame;
    private String message = "";
    private Color messageColor = Color.WHITE;

    public BattlePanel(GameFrame frame) {
        this.frame = frame;
        setLayout(null);
        setFocusable(true);

        Rectangle box = GameConfig.QUESTION_BOX;
        questionPanel.setBounds(box);
        add(questionPanel);

        attackButton.setBounds(GameConfig.WIDTH / 2 - 90, box.y + box.height + 6, 180, 46);
        attackButton.addActionListener(e -> submitAnswer());
        add(attackButton);

        GameButton backButton = new GameButton("ยอมแพ้", new Color(200, 200, 210), GameConfig.DARK, 13);
        backButton.setBounds(GameConfig.WIDTH / 2 - 50, GameConfig.HEIGHT - 44, 100, 34);
        backButton.addActionListener(e -> quitBattle());
        add(backButton);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (obstacle != null) {
                    obstacle.handleClick(e.getX(), e.getY());
                }
            }
        });
    }

    public void startLevel(Level newLevel) {
        stopThreads();
        level = newLevel;
        player = new Player();
        enemy = level.createEnemy();
        obstacle = level.createObstacle();
        battle = new Battle(player, enemy, level.createQuestions());
        effects.clear();
        pendingAttack = null;

        obstacle.start();
        gameLoop = new GameLoop(this);
        gameLoop.start();
        nextQuestion();
    }

    private void nextQuestion() {
        question = battle.nextQuestion();
        int seconds = obstacle.adjustTimeLimit(level.getTimeLimit(), battle.getWrongStreak());
        questionPanel.showQuestion(question, battle.getQuestionNumber(), seconds);
        question.setSubmitAction(e -> submitAnswer());
        question.focusInput();
        attackButton.setEnabled(true);
        showMessage(obstacle.getName() + " : " + obstacle.getDescription(), Color.WHITE);

        state = State.ANSWERING;
        countdown = new CountdownThread(seconds, this);
        countdown.start();
    }

    private void submitAnswer() {
        if (state != State.ANSWERING) {
            return;
        }
        if (!question.hasAnswer()) {
            showMessage("ยังไม่ได้ตอบเลย!", GameConfig.YELLOW);
            return;
        }
        resolveAnswer(question.isCorrect(), false);
    }

    @Override
    public void onTick(int secondsLeft) {
        questionPanel.setTimeLeft(secondsLeft);
    }

    @Override
    public void onTimeUp() {
        if (state == State.ANSWERING) {
            resolveAnswer(false, true);
        }
    }

    private void resolveAnswer(boolean correct, boolean timeUp) {
        lockInput();
        Attack attack = battle.submitAnswer(correct);
        pendingAttack = attack;
        animationFrame = 0;

        if (attack.isSpecial()) {
            attack.getAttacker().startCast();
            effects.add(new EnergyBall(attack.getAttacker(), attack.getTarget(), SPECIAL_HIT_FRAME));
            hitFrame = SPECIAL_HIT_FRAME;
        } else {
            attack.getAttacker().startLunge();
            hitFrame = NORMAL_HIT_FRAME;
        }

        if (correct) {
            endFrame = CORRECT_PAUSE_FRAMES;
            String text = attack.isSpecial()
                ? "ถูก 3 ข้อติด! ท่าไม้ตาย " + player.getSpecialName() + "!"
                : "ถูกต้อง!";
            showMessage(text, GameConfig.GREEN);
        } else {
            endFrame = WRONG_PAUSE_FRAMES;
            String reason = timeUp ? "หมดเวลา!" : "ผิด!";
            String special = attack.isSpecial() ? "  ศัตรูใช้ " + enemy.getSpecialName() + "!" : "";
            showMessage(reason + " เฉลย: " + question.getCorrectAnswer() + special, GameConfig.RED);
        }
    }

    private void lockInput() {
        state = State.ANIMATING;
        cancelCountdown();
        attackButton.setEnabled(false);
        questionPanel.setInputEnabled(false);
    }

    private void showMessage(String text, Color color) {
        message = text;
        messageColor = color;
    }

    @Override
    public void update() {
        if (state == State.FINISHED) {
            return;
        }
        player.update();
        enemy.update();
        obstacle.update();
        updateEffects();

        double obstacleDamage = obstacle.collectDamage();
        if (obstacleDamage > 0) {
            player.takeDamage(obstacleDamage);
            addDamageText(player, obstacleDamage);
        }

        if (state == State.ANSWERING && player.isDead()) {
            // ตายจากอุปสรรคระหว่างที่ยังตอบไม่เสร็จ
            lockInput();
            pendingAttack = null;
            animationFrame = 0;
            endFrame = DEATH_PAUSE_FRAMES;
        }
        if (state == State.ANIMATING) {
            updateAnimation();
        }
        repaint();
    }

    private void updateEffects() {
        Iterator<Effect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            effect.update();
            if (effect.isFinished()) {
                iterator.remove();
            }
        }
    }

    private void updateAnimation() {
        animationFrame++;
        if (pendingAttack != null && animationFrame == hitFrame) {
            pendingAttack.apply();
            addDamageText(pendingAttack.getTarget(), pendingAttack.getDamage());
            pendingAttack = null;
        }
        if (animationFrame >= endFrame) {
            if (battle.isOver()) {
                finishBattle();
            } else {
                nextQuestion();
            }
        }
    }

    private void addDamageText(GameCharacter target, double damage) {
        effects.add(new FloatingText("-" + damage, target.getCenterX(), target.getCenterY() - 70,
            GameConfig.RED));
    }

    private void finishBattle() {
        state = State.FINISHED;
        stopThreads();
        frame.showResult(level, battle.isPlayerWinner(), battle.getCorrectCount(),
            battle.getWrongCount(), player.getHp());
    }

    private void quitBattle() {
        state = State.FINISHED;
        stopThreads();
        frame.showLevelSelect();
    }

    private void cancelCountdown() {
        if (countdown != null) {
            countdown.cancel();
            countdown = null;
        }
    }

    private void stopThreads() {
        cancelCountdown();
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
        if (obstacle != null) {
            obstacle.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (level == null) {
            return;
        }
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        level.draw(g);
        drawLevelInfo(g);
        player.draw(g);
        enemy.draw(g);
        for (Effect effect : effects) {
            effect.draw(g);
        }
        drawName(g, player, "คอมโบ", battle.getCorrectStreak(), GameConfig.YELLOW);
        drawName(g, enemy, "โกรธ", battle.getWrongStreak(), GameConfig.RED);
        drawMessage(g);
    }

    /** อุปสรรคต้องอยู่บนสุด จึงวาดหลังจากวาด component ลูกทั้งหมดแล้ว */
    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (obstacle != null) {
            obstacle.draw((Graphics2D) g);
        }
    }

    private void drawLevelInfo(Graphics2D g) {
        Rectangle box = GameConfig.QUESTION_BOX;
        int y = box.y + box.height + 34;
        g.setFont(GameConfig.textFont(15));
        g.setColor(GameConfig.DARK);
        g.drawString("ด่าน: " + level.getName(), box.x + 1, y + 1);
        g.setColor(Color.WHITE);
        g.drawString("ด่าน: " + level.getName(), box.x, y);
    }

    private void drawName(Graphics2D g, GameCharacter character, String label, int streak, Color color) {
        int y = GameConfig.GROUND_Y + 54;
        String text = character.getName();
        g.setFont(GameConfig.textFont(14));
        int x = character.getCenterX() - g.getFontMetrics().stringWidth(text) / 2;
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);

        // จุดนับการตอบติดกัน ครบ 3 จุดใช้ท่าไม้ตาย
        int dotsX = character.getCenterX() - 60;
        int dotsY = y + 12;
        g.drawString(label, dotsX, dotsY + 11);
        for (int i = 0; i < Battle.STREAK_FOR_SPECIAL; i++) {
            int dotX = dotsX + 60 + i * 20;
            g.setColor(GameConfig.DARK);
            g.fillOval(dotX, dotsY, 14, 14);
            if (i < streak) {
                g.setColor(color);
                g.fillOval(dotX + 2, dotsY + 2, 10, 10);
            }
            g.setColor(Color.WHITE);
        }
    }

    private void drawMessage(Graphics2D g) {
        g.setFont(GameConfig.textFont(18));
        int width = g.getFontMetrics().stringWidth(message) + 40;
        int x = (GameConfig.WIDTH - width) / 2;
        int y = GameConfig.QUESTION_BOX.y + GameConfig.QUESTION_BOX.height + 58;
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRoundRect(x, y, width, 34, 14, 14);
        g.setColor(messageColor);
        g.drawString(message, x + 20, y + 24);
    }
}
