import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** หน้าเริ่มเกม */
public class MenuPanel extends JPanel implements Updatable {
    private static final String HOW_TO_PLAY =
        "ตอบโจทย์ให้ถูกและทันเวลาเพื่อโจมตีศัตรู\n\n"
        + "- โจทย์เติมคำ: คลิกช่องว่าง พิมพ์คำตอบ แล้วกด Attack (หรือ Enter)\n"
        + "- โจทย์ช้อยส์: คลิกเลือกข้อที่ถูก แล้วกด Attack\n"
        + "- ตอบผิดหรือหมดเวลา ศัตรูจะตีเรา\n"
        + "- ถูก 3 ข้อติด ใช้ท่าไม้ตายดาเมจ 1.5 (ปกติ 1)\n"
        + "- ผิด 3 ข้อติด ศัตรูก็ใช้ท่าไม้ตายใส่เราเหมือนกัน\n"
        + "- เลือดฝั่งละ 5 หน่วย ฝั่งไหนหมดก่อนแพ้\n"
        + "- มี 3 ด่าน แต่ละด่านมีโจทย์และอุปสรรคไม่เหมือนกัน";

    private final Level backgroundLevel = new GrasslandLevel();
    private final Player player = new Player();
    private final Enemy enemy = new SilverKnight();

    public MenuPanel(GameFrame frame) {
        setLayout(null);

        GameButton startButton = new GameButton("S T A R T", GameConfig.YELLOW, GameConfig.DARK, 30);
        startButton.setBounds(GameConfig.WIDTH / 2 - 150, 300, 300, 76);
        startButton.addActionListener(e -> frame.showLevelSelect());
        add(startButton);

        GameButton helpButton = new GameButton("วิธีเล่น", Color.WHITE, GameConfig.DARK, 16);
        helpButton.setBounds(GameConfig.WIDTH / 2 - 75, 392, 150, 42);
        helpButton.addActionListener(e -> JOptionPane.showMessageDialog(this, HOW_TO_PLAY, "วิธีเล่น",
            JOptionPane.INFORMATION_MESSAGE));
        add(helpButton);

        new GameLoop(this).start();
    }

    @Override
    public void update() {
        if (isShowing()) { // อยู่หน้าอื่นก็ไม่ต้องวาด
            player.update();
            enemy.update();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        backgroundLevel.draw(g);

        drawCentered(g, "แก้โจทย์ต่อสู้", 64, 130);
        drawCentered(g, "Problem Battle", 34, 190);

        player.drawBody(g);
        enemy.drawBody(g);
    }

    private void drawCentered(Graphics2D g, String text, int size, int y) {
        g.setFont(GameConfig.textFont(size));
        int x = (GameConfig.WIDTH - g.getFontMetrics().stringWidth(text)) / 2;
        g.setColor(GameConfig.DARK);
        g.drawString(text, x + 3, y + 3);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }
}
