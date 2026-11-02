import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/** ด่าน 1: ฝนตก ยิ่งตอบผิดติดกัน เวลาข้อถัดไปยิ่งน้อยลง */
public class RainObstacle extends Obstacle {
    private static final int DROPS = 110;
    private static final int PENALTY_SECONDS = 5;
    private static final int MIN_SECONDS = 10;

    private final int[] dropX = new int[DROPS];
    private final int[] dropY = new int[DROPS];

    public RainObstacle() {
        Random random = new Random();
        for (int i = 0; i < DROPS; i++) {
            dropX[i] = random.nextInt(GameConfig.WIDTH);
            dropY[i] = random.nextInt(GameConfig.HEIGHT);
        }
    }

    @Override
    public String getName() {
        return "ฝนตกหนัก";
    }

    @Override
    public String getDescription() {
        return "ตอบผิดติดกัน เวลาข้อถัดไปจะลดลงข้อละ " + PENALTY_SECONDS + " วินาที";
    }

    @Override
    public int adjustTimeLimit(int baseSeconds, int wrongStreak) {
        return Math.max(MIN_SECONDS, baseSeconds - wrongStreak * PENALTY_SECONDS);
    }

    @Override
    public void update() {
        for (int i = 0; i < DROPS; i++) {
            dropX[i] -= 3;
            dropY[i] += 16;
            if (dropY[i] > GameConfig.HEIGHT) {
                dropY[i] = 0;
            }
            if (dropX[i] < 0) {
                dropX[i] = GameConfig.WIDTH;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(200, 225, 255, 130));
        for (int i = 0; i < DROPS; i++) {
            g.drawLine(dropX[i], dropY[i], dropX[i] - 3, dropY[i] + 14);
        }
    }
}
