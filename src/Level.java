import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * class แม่ของด่าน แต่ละด่านกำหนดโจทย์ ศัตรู อุปสรรค เวลา และฉากของตัวเอง
 * เพิ่มด่านใหม่ได้โดยสร้าง class ลูกเพิ่ม 1 ตัว แล้วใส่ใน GameFrame
 */
public abstract class Level implements Drawable {
    private BufferedImage background;

    public abstract String getName();

    public abstract String getTopic();

    public abstract int getTimeLimit();

    public abstract Enemy createEnemy();

    public abstract Obstacle createObstacle();

    public abstract List<Question> createQuestions();

    protected abstract void paintBackground(Graphics2D g);

    /** วาดฉากลงรูปไว้ครั้งเดียว เฟรมต่อไปใช้รูปเดิม จะได้ไม่ต้องคำนวณใหม่ */
    @Override
    public void draw(Graphics2D g) {
        if (background == null) {
            background = new BufferedImage(GameConfig.WIDTH, GameConfig.HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D imageGraphics = background.createGraphics();
            paintBackground(imageGraphics);
            imageGraphics.dispose();
        }
        g.drawImage(background, 0, 0, null);
    }

    protected void drawSky(Graphics2D g, Color top, Color bottom) {
        g.setPaint(new java.awt.GradientPaint(0, 0, top, 0, GameConfig.GROUND_Y, bottom));
        g.fillRect(0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);
    }

    /** ภูเขาแบบขั้นบันไดให้ดูเป็น pixel art */
    protected void drawMountains(Graphics2D g, int height, int spacing, int shift, Color color) {
        int step = 8;
        g.setColor(color);
        for (int x = 0; x < GameConfig.WIDTH; x += step) {
            int distance = Math.abs((x + shift) % spacing - spacing / 2);
            int h = height - distance * 2 * height / spacing;
            h = h / step * step;
            g.fillRect(x, GameConfig.GROUND_Y - 60 - h, step, h + 60);
        }
    }

    protected void drawCloudBand(Graphics2D g, int y, int size, Color color) {
        g.setColor(color);
        for (int x = -size / 2; x < GameConfig.WIDTH; x += size * 3 / 4) {
            g.fillOval(x, y, size, size);
        }
        g.fillRect(0, y + size / 2, GameConfig.WIDTH, GameConfig.GROUND_Y - y);
    }

    protected void drawGround(Graphics2D g, Color top, Color body, Color tuft) {
        g.setColor(body);
        g.fillRect(0, GameConfig.GROUND_Y, GameConfig.WIDTH, GameConfig.HEIGHT - GameConfig.GROUND_Y);
        g.setColor(top);
        g.fillRect(0, GameConfig.GROUND_Y, GameConfig.WIDTH, 5);
        g.setColor(tuft);
        Random random = new Random(7); // ใช้ seed เดิม หญ้าจะอยู่ที่เดิมทุกครั้ง
        for (int i = 0; i < 70; i++) {
            int x = random.nextInt(GameConfig.WIDTH);
            int y = GameConfig.GROUND_Y + 10 + random.nextInt(85);
            g.fillRect(x, y, 3, 3);
            g.fillRect(x + 6, y, 3, 3);
            g.fillRect(x + 3, y - 3, 3, 3);
        }
    }
}
