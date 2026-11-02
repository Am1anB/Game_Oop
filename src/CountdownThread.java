import javax.swing.SwingUtilities;

/** thread นับถอยหลังเวลาตอบของแต่ละข้อ สร้างใหม่ทุกข้อ */
public class CountdownThread extends Thread {

    /** ผู้ที่อยากรู้ว่าเวลาเดินไปถึงไหนแล้ว */
    public interface Listener {
        void onTick(int secondsLeft);

        void onTimeUp();
    }

    private final Listener listener;
    private final int totalSeconds;
    private volatile boolean cancelled;

    public CountdownThread(int totalSeconds, Listener listener) {
        super("countdown");
        this.totalSeconds = totalSeconds;
        this.listener = listener;
        setDaemon(true);
    }

    public void cancel() {
        cancelled = true;
        interrupt();
    }

    @Override
    public void run() {
        for (int left = totalSeconds - 1; left >= 0 && !cancelled; left--) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
            final int secondsLeft = left;
            SwingUtilities.invokeLater(() -> notifyListener(secondsLeft));
        }
    }

    private void notifyListener(int secondsLeft) {
        // อาจถูกยกเลิกไปแล้วระหว่างที่งานนี้รอคิวอยู่
        if (cancelled) {
            return;
        }
        listener.onTick(secondsLeft);
        if (secondsLeft == 0) {
            listener.onTimeUp();
        }
    }
}
