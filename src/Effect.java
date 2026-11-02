/** เอฟเฟกต์ชั่วคราวบนจอ พอจบแล้ว BattlePanel จะลบทิ้งเอง */
public abstract class Effect implements Drawable, Updatable {
    private boolean finished;

    public boolean isFinished() {
        return finished;
    }

    protected void finish() {
        finished = true;
    }
}
