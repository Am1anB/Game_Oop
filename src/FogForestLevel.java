import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

/** ด่าน 2: ป่าหมอก */
public class FogForestLevel extends Level {

    @Override
    public String getName() {
        return "ป่าหมอกมรณะ";
    }

    @Override
    public String getTopic() {
        return "Inheritance / Abstract / Interface";
    }

    @Override
    public int getTimeLimit() {
        return 25;
    }

    @Override
    public Enemy createEnemy() {
        return new ForestWitch();
    }

    @Override
    public Obstacle createObstacle() {
        return new FogObstacle();
    }

    @Override
    public List<Question> createQuestions() {
        return QuestionBank.inheritance();
    }

    @Override
    protected void paintBackground(Graphics2D g) {
        drawSky(g, new Color(30, 60, 70), new Color(130, 170, 160));
        drawMountains(g, 180, 340, 90, new Color(45, 85, 90));
        for (int x = -20; x < GameConfig.WIDTH; x += 75) {
            drawPine(g, x, 90 + (x * 7 % 60 + 60) % 60, new Color(25, 65, 55));
        }
        for (int x = 20; x < GameConfig.WIDTH; x += 110) {
            drawPine(g, x, 150 + (x * 3 % 50 + 50) % 50, new Color(15, 45, 40));
        }
        drawGround(g, new Color(30, 70, 50), new Color(50, 100, 70), new Color(35, 80, 55));
    }

    private void drawPine(Graphics2D g, int x, int height, Color color) {
        int step = 8;
        g.setColor(color);
        for (int y = 0; y < height; y += step) {
            int halfWidth = (y / step + 1) * step / 3 + 4;
            g.fillRect(x - halfWidth, GameConfig.GROUND_Y - height + y, halfWidth * 2, step);
        }
    }
}
