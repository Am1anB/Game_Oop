import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

/** ด่าน 3: ปล่องภูเขาไฟ (ด่านบอส) */
public class VolcanoLevel extends Level {

    @Override
    public String getName() {
        return "ปล่องภูเขาไฟ";
    }

    @Override
    public String getTopic() {
        return "Polymorphism / Exception / Thread";
    }

    @Override
    public int getTimeLimit() {
        return 20;
    }

    @Override
    public Enemy createEnemy() {
        return new DemonLord();
    }

    @Override
    public Obstacle createObstacle() {
        return new MeteorObstacle();
    }

    @Override
    public List<Question> createQuestions() {
        return QuestionBank.advanced();
    }

    @Override
    protected void paintBackground(Graphics2D g) {
        drawSky(g, new Color(50, 15, 25), new Color(235, 110, 40));
        drawMountains(g, 200, 420, 60, new Color(70, 30, 35));
        drawMountains(g, 130, 260, 170, new Color(40, 18, 25));

        // ธารลาวาไหลลงจากยอดเขา
        g.setColor(new Color(255, 130, 30));
        for (int y = 300; y < GameConfig.GROUND_Y; y += 8) {
            int wave = (y / 8 % 4 < 2) ? 0 : 8;
            g.fillRect(150 + wave, y, 10, 8);
            g.fillRect(800 - wave, y + 60, 10, 8);
        }
        drawGround(g, new Color(255, 120, 30), new Color(55, 30, 30), new Color(200, 80, 30));
    }
}
