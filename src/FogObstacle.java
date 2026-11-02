import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

/** ด่าน 2: กลุ่มหมอกลอยผ่านกล่องโจทย์ ต้องรอให้หมอกผ่านไปถึงจะอ่านโจทย์ได้ครบ */
public class FogObstacle extends Obstacle {
    private static final int BANK_WIDTH = 300;
    private static final int GAP = 200;
    private static final double SPEED = 2.5;
    private static final Color FOG = new Color(225, 232, 232, 235);

    private double offset;

    @Override
    public String getName() {
        return "หมอกหนาทึบ";
    }

    @Override
    public String getDescription() {
        return "หมอกลอยบังโจทย์ รอให้หมอกผ่านไปก่อนค่อยอ่าน";
    }

    @Override
    public void update() {
        offset = (offset + SPEED) % (BANK_WIDTH + GAP);
    }

    @Override
    public void draw(Graphics2D g) {
        Rectangle box = GameConfig.QUESTION_BOX;
        Shape oldClip = g.getClip();
        g.clipRect(box.x, box.y, box.width, box.height); // ไม่ให้หมอกล้นออกนอกกล่องโจทย์

        for (int x = box.x - BANK_WIDTH - GAP; x < box.x + box.width; x += BANK_WIDTH + GAP) {
            g.setColor(FOG);
            g.fillOval(x + (int) offset, box.y - 20, BANK_WIDTH, box.height + 40);
        }
        g.setClip(oldClip);
    }
}
