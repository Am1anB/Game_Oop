import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * class แม่ของโจทย์ทุกแบบ
 * BattlePanel เรียกใช้ผ่าน Question อย่างเดียว ไม่ต้องรู้ว่าเป็นโจทย์แบบไหน (polymorphism)
 */
public abstract class Question {
    protected static final Color TEXT_COLOR = new Color(235, 238, 245);

    private final String title;
    private final String code;

    protected Question(String title, String code) {
        this.title = title;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    protected String getCode() {
        return code;
    }

    /** สร้างช่องสำหรับตอบ ถูกเรียกทุกครั้งที่โจทย์ข้อนี้ขึ้นจอ คำตอบเก่าจึงถูกล้างไปด้วย */
    public abstract JComponent createInputArea();

    public abstract boolean hasAnswer();

    public abstract boolean isCorrect();

    public abstract String getCorrectAnswer();

    /** โจทย์แบบพิมพ์ override เพื่อให้เคอร์เซอร์ไปรออยู่ในช่องทันที */
    public void focusInput() {
    }

    /** โจทย์แบบไหนส่งคำตอบจากช่องตอบได้เอง ให้ override method นี้ */
    public void setSubmitAction(ActionListener action) {
    }

    protected JPanel createColumn() {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setOpaque(false);
        return column;
    }

    protected JLabel createCodeLabel(String text) {
        // บรรทัดว่างต้องมีช่องว่าง 1 ตัว ไม่อย่างนั้น JLabel จะไม่มีความสูง
        JLabel label = new JLabel(text.isEmpty() ? " " : text);
        label.setFont(GameConfig.codeFont(16));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}
