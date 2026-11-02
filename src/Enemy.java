/** ศัตรูทุกตัวยืนฝั่งขวาและหันซ้าย รายละเอียดอื่นให้ class ลูกกำหนด */
public abstract class Enemy extends GameCharacter {

    protected Enemy(String name, String assetFolder) {
        super(name, GameConfig.WIDTH - 172, assetFolder);
    }

    @Override
    protected final boolean facesRight() {
        return false;
    }
}
