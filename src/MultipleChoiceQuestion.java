import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/** โจทย์แบบช้อยส์ 4 ตัวเลือก */
public class MultipleChoiceQuestion extends Question {
    private final String[] choices;
    private final int correctIndex;
    private JRadioButton[] buttons;

    public MultipleChoiceQuestion(String title, String code, String[] choices, int correctIndex) {
        super(title, code);
        this.choices = choices;
        this.correctIndex = correctIndex;
    }

    @Override
    public JComponent createInputArea() {
        JPanel column = createColumn();
        if (!getCode().isEmpty()) {
            for (String line : getCode().split("\n")) {
                column.add(createCodeLabel(line));
            }
        }

        JPanel grid = new JPanel(new GridLayout(2, 2, 10, 2));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        ButtonGroup group = new ButtonGroup();
        buttons = new JRadioButton[choices.length];
        for (int i = 0; i < choices.length; i++) {
            buttons[i] = new JRadioButton(choices[i]);
            buttons[i].setFont(GameConfig.textFont(15));
            buttons[i].setForeground(TEXT_COLOR);
            buttons[i].setOpaque(false);
            buttons[i].setFocusable(false);
            group.add(buttons[i]);
            grid.add(buttons[i]);
        }
        column.add(grid);
        return column;
    }

    @Override
    public boolean hasAnswer() {
        for (JRadioButton button : buttons) {
            if (button.isSelected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isCorrect() {
        return buttons[correctIndex].isSelected();
    }

    @Override
    public String getCorrectAnswer() {
        return choices[correctIndex];
    }
}
