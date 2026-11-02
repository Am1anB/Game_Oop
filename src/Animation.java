import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * ชุดรูปของ 1 ท่า เช่น ท่าวิ่ง วนเปลี่ยนรูปไปเรื่อย ๆ ให้ดูเหมือนตัวละครขยับได้
 * รูปอยู่ที่ assets/ชื่อตัวละคร/ชื่อท่า_0.png, _1.png, ... มีกี่รูปก็ได้ โหลดจนกว่าจะไม่เจอไฟล์
 */
public class Animation {
    private final BufferedImage[] frames;
    private final int delay; // แต่ละรูปแสดงค้างไว้กี่เฟรมของเกม
    private final boolean loop;
    private int tick;

    public Animation(String folder, String pose, int delay, boolean loop) {
        this.frames = loadFrames(folder, pose);
        this.delay = delay;
        this.loop = loop;
    }

    private static BufferedImage[] loadFrames(String folder, String pose) {
        List<BufferedImage> images = new ArrayList<>();
        try {
            File file = new File("assets/" + folder, pose + "_0.png");
            while (file.exists()) {
                images.add(ImageIO.read(file));
                file = new File("assets/" + folder, pose + "_" + images.size() + ".png");
            }
        } catch (IOException e) {
            throw new IllegalStateException("อ่านรูปไม่ได้: " + folder + "/" + pose, e);
        }
        if (images.isEmpty()) {
            throw new IllegalStateException("ไม่พบรูป assets/" + folder + "/" + pose + "_0.png");
        }
        return images.toArray(new BufferedImage[0]);
    }

    public void reset() {
        tick = 0;
    }

    public void update() {
        tick++;
    }

    public BufferedImage getCurrentFrame() {
        int index = tick / delay;
        if (loop) {
            return frames[index % frames.length];
        }
        return frames[Math.min(index, frames.length - 1)]; // ไม่วนซ้ำ ค้างที่รูปสุดท้าย
    }

    public boolean isFinished() {
        return !loop && tick >= delay * frames.length;
    }
}
