import java.awt.Color;
import java.awt.Graphics2D;

/** ลูกพลังของท่าไม้ตาย พุ่งจากผู้โจมตีไปหาเป้าหมาย */
public class EnergyBall extends Effect {
    private static final int RADIUS = 30;

    private final double stepX;
    private final double stepY;
    private final Color color;
    private final int totalFrames;
    private double x;
    private double y;
    private int frame;

    public EnergyBall(GameCharacter from, GameCharacter to, int totalFrames) {
        this.x = from.getCenterX();
        this.y = from.getCenterY();
        this.stepX = (double) (to.getCenterX() - from.getCenterX()) / totalFrames;
        this.stepY = (double) (to.getCenterY() - from.getCenterY()) / totalFrames;
        this.color = from.getSpecialColor();
        this.totalFrames = totalFrames;
    }

    @Override
    public void update() {
        x += stepX;
        y += stepY;
        frame++;
        if (frame >= totalFrames) {
            finish();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int) x - RADIUS, (int) y - RADIUS, RADIUS * 2, RADIUS * 2);
    }
}
