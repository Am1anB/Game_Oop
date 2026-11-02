/**
 * class แม่ของอุปสรรคประจำด่าน
 * method ที่ไม่ได้เป็น abstract คือพฤติกรรมปกติ "ไม่ทำอะไร"
 * อุปสรรคแต่ละแบบ override เฉพาะเรื่องที่ตัวเองเกี่ยวข้อง
 */
public abstract class Obstacle implements Drawable, Updatable {

    public abstract String getName();

    public abstract String getDescription();

    /** ปรับเวลาตอบของข้อถัดไป */
    public int adjustTimeLimit(int baseSeconds, int wrongStreak) {
        return baseSeconds;
    }

    /** ดาเมจที่ผู้เล่นโดนจากอุปสรรคตั้งแต่ถามครั้งก่อน */
    public double collectDamage() {
        return 0;
    }

    public void handleClick(int x, int y) {
    }

    public void start() {
    }

    public void stop() {
    }
}
