import java.awt.Color;

/** ตัวละครฝั่งเรา */
public class Player extends GameCharacter {

    public Player() {
        super("อัศวินน้ำเงิน", 172, "player");
    }

    @Override
    protected boolean facesRight() {
        return true;
    }

    @Override
    public String getSpecialName() {
        return "ดาบสายฟ้า";
    }

    @Override
    public Color getSpecialColor() {
        return new Color(90, 220, 255);
    }
}
