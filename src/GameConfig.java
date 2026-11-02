import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

/** ค่าคงที่ที่ใช้ร่วมกันทั้งเกม */
public final class GameConfig {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 640;
    public static final int FRAME_DELAY = 33; // ประมาณ 30 เฟรมต่อวินาที
    public static final int GROUND_Y = 545;

    // กล่องโจทย์ด้านบนของฉากต่อสู้
    public static final Rectangle QUESTION_BOX = new Rectangle(50, 12, 860, 262);

    public static final Color PANEL_COLOR = new Color(68, 76, 94, 235);
    public static final Color DARK = new Color(26, 26, 46);
    public static final Color YELLOW = new Color(255, 209, 77);
    public static final Color RED = new Color(240, 60, 50);
    public static final Color GREEN = new Color(90, 200, 110);

    private GameConfig() {
    }

    public static Font textFont(int size) {
        return new Font("Tahoma", Font.BOLD, size);
    }

    public static Font codeFont(int size) {
        return new Font(Font.MONOSPACED, Font.BOLD, size);
    }
}
