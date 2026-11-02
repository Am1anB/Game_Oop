import java.awt.Color;

/** ศัตรูด่าน 1 */
public class SilverKnight extends Enemy {

    public SilverKnight() {
        super("อัศวินเงิน", "silver_knight");
    }

    @Override
    public String getSpecialName() {
        return "โล่กระแทก";
    }

    @Override
    public Color getSpecialColor() {
        return new Color(235, 235, 245);
    }
}
