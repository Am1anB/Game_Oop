import javax.swing.SwingUtilities;

/**
 * thread หลักของเกม ทำหน้าที่สั่งอัปเดตทุกเฟรม
 * ตัวงานจริงส่งไปทำบน Event Dispatch Thread เพราะ Swing ต้องทำงานบน thread นั้น
 */
public class GameLoop implements Runnable {
    private final Updatable target;
    private volatile boolean running;

    public GameLoop(Updatable target) {
        this.target = target;
    }

    public void start() {
        running = true;
        Thread thread = new Thread(this, "game-loop");
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            SwingUtilities.invokeLater(target::update);
            try {
                Thread.sleep(GameConfig.FRAME_DELAY);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
