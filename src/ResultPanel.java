import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/** หน้าสรุปผลหลังจบด่าน */
public class ResultPanel extends JPanel {
    private final GameButton nextButton = new GameButton("ด่านถัดไป", GameConfig.YELLOW, GameConfig.DARK, 18);
    private final GameButton retryButton = new GameButton("เล่นด่านนี้อีกครั้ง", Color.WHITE, GameConfig.DARK, 16);

    private Level level;
    private String headline = "";
    private Color headlineColor = Color.WHITE;
    private String summary = "";
    private String stats = "";
    private int levelIndex;

    public ResultPanel(GameFrame frame) {
        setLayout(null);

        nextButton.setBounds(GameConfig.WIDTH / 2 - 110, 340, 220, 54);
        nextButton.addActionListener(e -> frame.startLevel(levelIndex + 1));
        add(nextButton);

        retryButton.setBounds(GameConfig.WIDTH / 2 - 110, 404, 220, 46);
        retryButton.addActionListener(e -> frame.startLevel(levelIndex));
        add(retryButton);

        GameButton selectButton = new GameButton("เลือกด่าน", Color.WHITE, GameConfig.DARK, 16);
        selectButton.setBounds(GameConfig.WIDTH / 2 - 110, 460, 220, 46);
        selectButton.addActionListener(e -> frame.showLevelSelect());
        add(selectButton);
    }

    public void showResult(Level level, int levelIndex, boolean hasNextLevel, boolean won,
                           int correct, int wrong, double hpLeft) {
        this.level = level;
        this.levelIndex = levelIndex;

        if (won && !hasNextLevel) {
            headline = "พิชิตทุกด่านแล้ว!";
            summary = "คุณเอาชนะจอมมารลาวาได้สำเร็จ";
        } else if (won) {
            headline = "ชนะ!";
            summary = "ผ่านด่าน " + level.getName();
        } else {
            headline = "แพ้แล้ว...";
            summary = "ทบทวนเนื้อหาเรื่อง " + level.getTopic() + " แล้วลองใหม่";
        }
        headlineColor = won ? GameConfig.YELLOW : GameConfig.RED;
        stats = "ตอบถูก " + correct + " ข้อ   ตอบผิด " + wrong + " ข้อ   เลือดเหลือ " + hpLeft;
        nextButton.setVisible(won && hasNextLevel);
        repaint();
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

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);

        drawCentered(g, headline, 60, headlineColor, 170);
        drawCentered(g, summary, 22, Color.WHITE, 230);
        drawCentered(g, stats, 18, Color.WHITE, 280);
    }

    private void drawCentered(Graphics2D g, String text, int size, Color color, int y) {
        g.setFont(GameConfig.textFont(size));
        g.setColor(color);
        g.drawString(text, (GameConfig.WIDTH - g.getFontMetrics().stringWidth(text)) / 2, y);
    }
}
