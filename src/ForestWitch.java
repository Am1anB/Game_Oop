import java.awt.Color;

/** ศัตรูด่าน 2 */
public class ForestWitch extends Enemy {

    public ForestWitch() {
        super("แม่มดป่าหมอก", "forest_witch");
    }

    @Override
    public String getSpecialName() {
        return "คำสาปหมอกพิษ";
    }

    @Override
    public Color getSpecialColor() {
        return new Color(170, 90, 240);
    }
}
