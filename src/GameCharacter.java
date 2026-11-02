import java.awt.Color;
import java.awt.Graphics2D;

/**
 * class แม่ของตัวละครทุกตัว เก็บเลือด ท่าทาง และ animation ที่เหมือนกัน
 * ส่วนที่ต่างกัน (ชื่อท่าไม้ตาย สีพลัง ทิศที่หัน) ให้ class ลูกเป็นคนกำหนด
 */
public abstract class GameCharacter implements Drawable, Updatable {

    private enum State { IDLE, RUN_OUT, ATTACK, RUN_BACK, CAST, HURT, DEAD }

    public static final double MAX_HP = 5;
    public static final double NORMAL_DAMAGE = 1;
    public static final double SPECIAL_DAMAGE = 1.5;

    private static final int RUN_FRAMES = 14;
    private static final int RUN_SPEED = 34;
    private static final int ATTACK_DELAY = 3;

    // จังหวะที่ดาเมจเข้า นับจากเริ่มท่า ให้ตรงกับรูปตอนฟันโดน (รูปที่ 3 ของท่าโจมตี)
    public static final int MELEE_HIT_FRAME = RUN_FRAMES + ATTACK_DELAY * 2;
    public static final int CAST_FRAMES = 24;

    private static final int IMAGE_WIDTH = 240;
    private static final int IMAGE_HEIGHT = 216;
    private static final int FEET_IN_IMAGE = 192; // ระยะจากขอบบนของรูปถึงเท้า
    private static final int BODY_HEIGHT = 144;
    private static final int BAR_WIDTH = 130;
    private static final int BAR_HEIGHT = 16;

    private final String name;
    private final int homeX; // จุดกึ่งกลางตัวละครตอนยืนอยู่ที่เดิม
    private final Animation idleAnimation;
    private final Animation runAnimation;
    private final Animation attackAnimation;
    private final Animation castAnimation;
    private final Animation hurtAnimation;
    private final Animation deadAnimation;

    private double hp = MAX_HP;
    private int x;
    private State state;
    private Animation animation;
    private int stateFrame;

    protected GameCharacter(String name, int homeX, String assetFolder) {
        this.name = name;
        this.homeX = homeX;
        this.x = homeX;
        idleAnimation = new Animation(assetFolder, "idle", 8, true);
        runAnimation = new Animation(assetFolder, "run", 3, true);
        attackAnimation = new Animation(assetFolder, "attack", ATTACK_DELAY, false);
        castAnimation = new Animation(assetFolder, "cast", 4, true);
        hurtAnimation = new Animation(assetFolder, "hurt", 6, false);
        deadAnimation = new Animation(assetFolder, "dead", 5, false);
        changeState(State.IDLE, idleAnimation);
    }

    protected abstract boolean facesRight();

    public abstract String getSpecialName();

    public abstract Color getSpecialColor();

    private void changeState(State newState, Animation newAnimation) {
        state = newState;
        animation = newAnimation;
        animation.reset();
        stateFrame = 0;
    }

    public String getName() {
        return name;
    }

    public double getHp() {
        return hp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public void takeDamage(double damage) {
        hp = Math.max(0, hp - damage);
        if (isDead()) {
            changeState(State.DEAD, deadAnimation);
        } else if (state == State.IDLE) {
            changeState(State.HURT, hurtAnimation); // กำลังวิ่งหรือฟันอยู่จะไม่โดนขัดท่า
        }
    }

    /** วิ่งเข้าไปฟัน แล้ววิ่งกลับ (โจมตีธรรมดา) */
    public void startLunge() {
        if (!isDead()) {
            changeState(State.RUN_OUT, runAnimation);
        }
    }

    /** ยืนร่ายพลัง (ท่าไม้ตาย) */
    public void startCast() {
        if (!isDead()) {
            changeState(State.CAST, castAnimation);
        }
    }

    public int getCenterX() {
        return homeX;
    }

    public int getCenterY() {
        return GameConfig.GROUND_Y - BODY_HEIGHT / 2;
    }

    @Override
    public void update() {
        animation.update();
        stateFrame++;
        int direction = facesRight() ? 1 : -1;

        switch (state) {
            case RUN_OUT:
                x += direction * RUN_SPEED;
                if (stateFrame >= RUN_FRAMES) {
                    changeState(State.ATTACK, attackAnimation);
                }
                break;
            case ATTACK:
                if (animation.isFinished()) {
                    changeState(State.RUN_BACK, runAnimation);
                }
                break;
            case RUN_BACK:
                x -= direction * RUN_SPEED;
                if (stateFrame >= RUN_FRAMES) {
                    x = homeX;
                    changeState(State.IDLE, idleAnimation);
                }
                break;
            case CAST:
                if (stateFrame >= CAST_FRAMES) {
                    changeState(State.IDLE, idleAnimation);
                }
                break;
            case HURT:
                if (animation.isFinished()) {
                    changeState(State.IDLE, idleAnimation);
                }
                break;
            default:
                break; // IDLE วนรูปไปเรื่อย ๆ ส่วน DEAD ค้างที่รูปสุดท้าย
        }
    }

    @Override
    public void draw(Graphics2D g) {
        drawBody(g);
        drawHpBar(g);
    }

    /** วาดเฉพาะตัวละคร ไม่มีหลอดเลือด ใช้ในหน้าเมนูด้วย */
    public void drawBody(Graphics2D g) {
        // ขากลับวิ่งหันหลังให้ศัตรู จึงกลับด้านรูป
        boolean drawFacingRight = facesRight() != (state == State.RUN_BACK);
        int imageY = GameConfig.GROUND_Y - FEET_IN_IMAGE;
        if (drawFacingRight) {
            g.drawImage(animation.getCurrentFrame(), x - IMAGE_WIDTH / 2, imageY,
                IMAGE_WIDTH, IMAGE_HEIGHT, null);
        } else {
            // ความกว้างติดลบ = วาดกลับด้านซ้าย-ขวา
            g.drawImage(animation.getCurrentFrame(), x + IMAGE_WIDTH / 2, imageY,
                -IMAGE_WIDTH, IMAGE_HEIGHT, null);
        }
    }

    private void drawHpBar(Graphics2D g) {
        int barX = getCenterX() - BAR_WIDTH / 2;
        int barY = GameConfig.GROUND_Y + 14;

        g.setColor(GameConfig.DARK);
        g.fillRoundRect(barX - 3, barY - 3, BAR_WIDTH + 6, BAR_HEIGHT + 6, 14, 14);
        g.setColor(new Color(90, 90, 100));
        g.fillRoundRect(barX, barY, BAR_WIDTH, BAR_HEIGHT, 10, 10);
        g.setColor(GameConfig.RED);
        g.fillRoundRect(barX, barY, (int) (BAR_WIDTH * hp / MAX_HP), BAR_HEIGHT, 10, 10);

        g.setColor(Color.WHITE);
        g.setFont(GameConfig.textFont(13));
        String text = hp + " / " + MAX_HP;
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, getCenterX() - textWidth / 2, barY + 13);
    }
}
