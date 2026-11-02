import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

/** ด่าน 1: ทุ่งหญ้า */
public class GrasslandLevel extends Level {

    @Override
    public String getName() {
        return "ทุ่งหญ้าสายฝน";
    }

    @Override
    public String getTopic() {
        return "Java พื้นฐาน";
    }

    @Override
    public int getTimeLimit() {
        return 30;
    }

    @Override
    public Enemy createEnemy() {
        return new SilverKnight();
    }

    @Override
    public Obstacle createObstacle() {
        return new RainObstacle();
    }

    @Override
    public List<Question> createQuestions() {
        return QuestionBank.basics();
    }

    @Override
    protected void paintBackground(Graphics2D g) {
        drawSky(g, new Color(80, 130, 230), new Color(150, 205, 245));
        drawMountains(g, 210, 300, 40, new Color(70, 150, 220));
        drawMountains(g, 150, 220, 130, new Color(40, 115, 195));
        drawCloudBand(g, 455, 70, new Color(120, 200, 240));
        drawCloudBand(g, 485, 80, Color.WHITE);
        drawGround(g, new Color(95, 140, 60), new Color(130, 180, 90), new Color(100, 150, 70));
    }
}
