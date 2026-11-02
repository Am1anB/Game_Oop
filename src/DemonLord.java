import java.awt.Color;

/** ศัตรูด่าน 3 (บอสใหญ่) */
public class DemonLord extends Enemy {

    public DemonLord() {
        super("จอมมารลาวา", "demon_lord");
    }

    @Override
    public String getSpecialName() {
        return "ลูกไฟนรก";
    }

    @Override
    public Color getSpecialColor() {
        return new Color(255, 120, 30);
    }
}
