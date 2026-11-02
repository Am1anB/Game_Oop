import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** หน้าเลือกด่าน แสดงการ์ดรายละเอียดของแต่ละด่าน */
public class LevelSelectPanel extends JPanel {
    private static final int CARD_WIDTH = 280;
    private static final int CARD_HEIGHT = 380;
    private static final int CARD_GAP = 26;
    private static final int CARD_Y = 110;

    private final Level backgroundLevel = new GrasslandLevel();

    public LevelSelectPanel(GameFrame frame, Level[] levels) {
        setLayout(null);

        int totalWidth = levels.length * CARD_WIDTH + (levels.length - 1) * CARD_GAP;
        int x = (GameConfig.WIDTH - totalWidth) / 2;
        for (int i = 0; i < levels.length; i++) {
            final int index = i;
            JPanel card = createCard(levels[i], i + 1);
            card.setBounds(x, CARD_Y, CARD_WIDTH, CARD_HEIGHT);

            GameButton playButton = new GameButton("เริ่มด่านนี้", GameConfig.YELLOW, GameConfig.DARK, 18);
            playButton.setBounds(50, CARD_HEIGHT - 66, CARD_WIDTH - 100, 50);
            playButton.addActionListener(e -> frame.startLevel(index));
            card.add(playButton);

            add(card);
            x += CARD_WIDTH + CARD_GAP;
        }

        GameButton backButton = new GameButton("กลับหน้าแรก", Color.WHITE, GameConfig.DARK, 15);
        backButton.setBounds(GameConfig.WIDTH / 2 - 80, 520, 160, 44);
        backButton.addActionListener(e -> frame.showMenu());
        add(backButton);
    }

    private JPanel createCard(Level level, int number) {
        Enemy enemy = level.createEnemy();
        Obstacle obstacle = level.createObstacle();

        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics;
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(GameConfig.DARK);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g.setColor(GameConfig.PANEL_COLOR);
                g.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 14, 14);
            }
        };
        card.setOpaque(false);

        JLabel title = createLabel("ด่าน " + number + " : " + level.getName(), 18, GameConfig.YELLOW);
        title.setBounds(18, 14, CARD_WIDTH - 36, 30);
        card.add(title);

        // ใช้ html เพื่อให้ JLabel ตัดบรรทัดอัตโนมัติ
        String detail = "<html><body style='width:" + (CARD_WIDTH - 90) + "px'>"
            + "<b>เนื้อหา:</b> " + level.getTopic() + "<br><br>"
            + "<b>ศัตรู:</b> " + enemy.getName() + "<br><br>"
            + "<b>เวลาต่อข้อ:</b> " + level.getTimeLimit() + " วินาที<br><br>"
            + "<b>อุปสรรค:</b> " + obstacle.getName() + "<br>" + obstacle.getDescription()
            + "</body></html>";
        JLabel detailLabel = createLabel(detail, 14, Color.WHITE);
        detailLabel.setVerticalAlignment(JLabel.TOP);
        detailLabel.setBounds(18, 54, CARD_WIDTH - 36, CARD_HEIGHT - 130);
        card.add(detailLabel);
        return card;
    }

    private JLabel createLabel(String text, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(GameConfig.textFont(size));
        label.setForeground(color);
        return label;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        backgroundLevel.draw(g);

        String title = "เลือกด่าน";
        g.setFont(GameConfig.textFont(42));
        int x = (GameConfig.WIDTH - g.getFontMetrics().stringWidth(title)) / 2;
        g.setColor(GameConfig.DARK);
        g.drawString(title, x + 2, 77);
        g.setColor(Color.WHITE);
        g.drawString(title, x, 75);
    }
}
