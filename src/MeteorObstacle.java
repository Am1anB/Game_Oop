import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * ด่าน 3: อุกกาบาตตกใส่ผู้เล่น ต้องคลิกทำลายก่อนถึงตัว
 * มี thread ของตัวเองคอยปล่อยอุกกาบาต ส่วนเกมอ่าน list เดียวกันจาก thread อื่น
 * ทุกครั้งที่แตะ list จึงต้องอยู่ใน synchronized
 */
public class MeteorObstacle extends Obstacle implements Runnable {
    private static final double METEOR_DAMAGE = 0.5;
    private static final int MIN_DELAY = 5000;
    private static final int MAX_DELAY = 8000;

    private final List<Meteor> meteors = new ArrayList<>();
    private final Random random = new Random();
    private volatile boolean running;
    private Thread thread;
    private double pendingDamage;

    @Override
    public String getName() {
        return "ฝนอุกกาบาต";
    }

    @Override
    public String getDescription() {
        return "คลิกอุกกาบาตก่อนตกใส่! ถ้าโดนจะเสียเลือด " + METEOR_DAMAGE;
    }

    @Override
    public void start() {
        running = true;
        thread = new Thread(this, "meteor-thread");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void stop() {
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(MIN_DELAY + random.nextInt(MAX_DELAY - MIN_DELAY));
            } catch (InterruptedException e) {
                return;
            }
            spawnMeteor();
        }
    }

    private void spawnMeteor() {
        int startX = 40 + random.nextInt(340);
        int startY = GameConfig.QUESTION_BOX.y + GameConfig.QUESTION_BOX.height + 20;
        Meteor meteor = new Meteor(startX, startY, 170, GameConfig.GROUND_Y - 80);
        synchronized (meteors) {
            meteors.add(meteor);
        }
    }

    @Override
    public void update() {
        synchronized (meteors) {
            Iterator<Meteor> iterator = meteors.iterator();
            while (iterator.hasNext()) {
                Meteor meteor = iterator.next();
                boolean wasExploding = meteor.isExploding();
                meteor.update();
                if (!wasExploding && meteor.hasHitPlayer()) {
                    pendingDamage += METEOR_DAMAGE;
                }
                if (meteor.isFinished()) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public double collectDamage() {
        double damage = pendingDamage;
        pendingDamage = 0;
        return damage;
    }

    @Override
    public void handleClick(int x, int y) {
        synchronized (meteors) {
            for (Meteor meteor : meteors) {
                if (meteor.contains(x, y)) {
                    meteor.explode();
                    return;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        synchronized (meteors) {
            for (Meteor meteor : meteors) {
                meteor.draw(g);
            }
        }
    }
}
