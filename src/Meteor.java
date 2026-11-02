import java.awt.Color;
import java.awt.Graphics2D;

/** อุกกาบาต 1 ลูก พุ่งจากบนจอลงมาหาผู้เล่น */
public class Meteor implements Drawable, Updatable {
    private static final int RADIUS = 18;
    private static final int CLICK_RADIUS = 34; // เผื่อระยะคลิกให้กดง่ายขึ้น
    private static final int FALL_FRAMES = 90;
    private static final int EXPLODE_FRAMES = 10;

    private final double stepX;
    private final double stepY;
    private double x;
    private double y;
    private int frame;
    private int explodeFrame;
    private boolean hitPlayer;

    public Meteor(int startX, int startY, int targetX, int targetY) {
        x = startX;
        y = startY;
        stepX = (double) (targetX - startX) / FALL_FRAMES;
        stepY = (double) (targetY - startY) / FALL_FRAMES;
    }

    public boolean isExploding() {
        return explodeFrame > 0;
    }

    public boolean isFinished() {
        return explodeFrame >= EXPLODE_FRAMES;
    }

    public boolean hasHitPlayer() {
        return hitPlayer;
    }

    /** ลูกที่ระเบิดอยู่แล้วคลิกซ้ำไม่ได้ */
    public boolean contains(int clickX, int clickY) {
        double dx = clickX - x;
        double dy = clickY - y;
        return !isExploding() && dx * dx + dy * dy <= CLICK_RADIUS * CLICK_RADIUS;
    }

    public void explode() {
        explodeFrame = 1;
    }

    @Override
    public void update() {
        if (isExploding()) {
            explodeFrame++;
            return;
        }
        x += stepX;
        y += stepY;
        frame++;
        if (frame >= FALL_FRAMES) {
            hitPlayer = true;
            explode();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        int cx = (int) x;
        int cy = (int) y;
        // ลูกที่ระเบิดแล้วเปลี่ยนเป็นสีเหลือง
        g.setColor(isExploding() ? GameConfig.YELLOW : new Color(120, 60, 40));
        g.fillOval(cx - RADIUS, cy - RADIUS, RADIUS * 2, RADIUS * 2);
    }
}
