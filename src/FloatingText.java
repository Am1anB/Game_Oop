import java.awt.Color;
import java.awt.Graphics2D;

/** ตัวเลขดาเมจที่ลอยขึ้นจากตัวละคร */
public class FloatingText extends Effect {
    private static final int LIFE_FRAMES = 35;

    private final String text;
    private final int x;
    private final Color color;
    private int y;
    private int frame;

    public FloatingText(String text, int x, int y, Color color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void update() {
        y -= 2;
        frame++;
        if (frame >= LIFE_FRAMES) {
            finish();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setFont(GameConfig.textFont(30));
        g.setColor(color);
        g.drawString(text, x - g.getFontMetrics().stringWidth(text) / 2, y);
    }
}
