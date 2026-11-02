/** การโจมตี 1 ครั้ง ใครตีใคร และเป็นท่าไม้ตายหรือไม่ */
public class Attack {
    private final GameCharacter attacker;
    private final GameCharacter target;
    private final boolean special;

    public Attack(GameCharacter attacker, GameCharacter target, boolean special) {
        this.attacker = attacker;
        this.target = target;
        this.special = special;
    }

    public GameCharacter getAttacker() {
        return attacker;
    }

    public GameCharacter getTarget() {
        return target;
    }

    public boolean isSpecial() {
        return special;
    }

    public double getDamage() {
        return special ? GameCharacter.SPECIAL_DAMAGE : GameCharacter.NORMAL_DAMAGE;
    }

    public void apply() {
        target.takeDamage(getDamage());
    }
}
