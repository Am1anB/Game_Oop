import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * กล่องโจทย์ มีแถบเวลา หัวข้อโจทย์ และช่องตอบ
 * ทุกอย่างในนี้ต้อง setOpaque(false) เพื่อให้ BattlePanel วาดหมอกทับได้ตลอด
 */
public class QuestionPanel extends JPanel {
    private static final int TIMER_BAR_HEIGHT = 12;
    private static final int WARNING_SECONDS = 5;

    private final JLabel titleLabel = new JLabel();
    private final JPanel inputHolder = new JPanel(new BorderLayout());
    private int questionNumber;
    private int timeLimit = 1;
    private int timeLeft;

    public QuestionPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createEmptyBorder(34, 18, 8, 18));

        titleLabel.setFont(GameConfig.textFont(17));
        titleLabel.setForeground(GameConfig.YELLOW);
        inputHolder.setOpaque(false);

        add(titleLabel, BorderLayout.NORTH);
        add(inputHolder, BorderLayout.CENTER);
    }

    public void showQuestion(Question question, int number, int seconds) {
        questionNumber = number;
        timeLimit = seconds;
        timeLeft = seconds;
        titleLabel.setText(question.getTitle());
        inputHolder.removeAll();
        inputHolder.add(question.createInputArea(), BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    public void setTimeLeft(int seconds) {
        timeLeft = seconds;
        repaint();
    }

    /** ล็อกช่องตอบทั้งหมดระหว่างเล่นอนิเมชันโจมตี */
    public void setInputEnabled(boolean enabled) {
        setEnabledDeep(inputHolder, enabled);
    }

    private void setEnabledDeep(Container container, boolean enabled) {
        for (Component child : container.getComponents()) {
            child.setEnabled(enabled);
            if (child instanceof Container) {
                setEnabledDeep((Container) child, enabled);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        g.setColor(GameConfig.DARK);
        g.fillRoundRect(0, 0, w, h, 14, 14);
        g.setColor(GameConfig.PANEL_COLOR);
        g.fillRoundRect(3, 3, w - 6, h - 6, 10, 10);

        g.setFont(GameConfig.textFont(14));
        g.setColor(Color.WHITE);
        g.drawString("ข้อที่ " + questionNumber, 18, 24);

        boolean warning = timeLeft <= WARNING_SECONDS;
        String timeText = timeLeft + " วินาที";
        g.setColor(warning ? GameConfig.RED : Color.WHITE);
        g.drawString(timeText, w - 18 - g.getFontMetrics().stringWidth(timeText), 24);

        int barX = 90;
        int barWidth = w - 200;
        g.setColor(GameConfig.DARK);
        g.fillRoundRect(barX, 13, barWidth, TIMER_BAR_HEIGHT, 8, 8);
        g.setColor(warning ? GameConfig.RED : GameConfig.GREEN);
        g.fillRoundRect(barX, 13, barWidth * timeLeft / timeLimit, TIMER_BAR_HEIGHT, 8, 8);
    }
}
