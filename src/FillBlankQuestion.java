import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** โจทย์เติมคำลงช่องว่าง ตำแหน่งช่องว่างในโค้ดเขียนด้วย ___ */
public class FillBlankQuestion extends Question {
    private static final String BLANK = "___";

    private final String[] answers;
    private JTextField field;

    /** answers ตัวแรกคือเฉลยหลัก ตัวที่เหลือคือคำตอบอื่นที่ถูกเหมือนกัน */
    public FillBlankQuestion(String title, String code, String... answers) {
        super(title, code);
        this.answers = answers;
    }

    @Override
    public JComponent createInputArea() {
        field = new JTextField(9);
        field.setFont(GameConfig.codeFont(16));
        field.setForeground(GameConfig.YELLOW);
        field.setCaretColor(GameConfig.YELLOW);
        field.setDisabledTextColor(GameConfig.YELLOW);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, GameConfig.YELLOW));

        JPanel column = createColumn();
        for (String line : getCode().split("\n")) {
            if (line.contains(BLANK)) {
                column.add(createBlankLine(line));
            } else {
                column.add(createCodeLabel(line));
            }
        }
        return column;
    }

    private JPanel createBlankLine(String line) {
        int blankIndex = line.indexOf(BLANK);
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        row.add(createCodeLabel(line.substring(0, blankIndex)));
        row.add(field);
        row.add(createCodeLabel(line.substring(blankIndex + BLANK.length())));
        return row;
    }

    @Override
    public boolean hasAnswer() {
        return !field.getText().trim().isEmpty();
    }

    @Override
    public boolean isCorrect() {
        String typed = field.getText().trim();
        if (typed.endsWith(";")) {
            typed = typed.substring(0, typed.length() - 1).trim();
        }
        for (String answer : answers) {
            if (typed.equals(answer)) { // Java แยกตัวพิมพ์เล็ก-ใหญ่ จึงใช้ equals ตรง ๆ
                return true;
            }
        }
        return false;
    }

    @Override
    public String getCorrectAnswer() {
        return answers[0];
    }

    @Override
    public void focusInput() {
        field.requestFocusInWindow();
    }

    @Override
    public void setSubmitAction(ActionListener action) {
        field.addActionListener(action); // กด Enter ในช่องพิมพ์ = โจมตี
    }
}
