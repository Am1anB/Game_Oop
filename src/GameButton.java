import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/** ปุ่มทรงมนสไตล์เกม วาดเองทั้งหมดใน paintComponent */
public class GameButton extends JButton {
    private final Color color;
    private final Color textColor;

    public GameButton(String text, Color color, Color textColor, int fontSize) {
        super(text);
        this.color = color;
        this.textColor = textColor;
        setFont(GameConfig.textFont(fontSize));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFocusable(false); // ไม่แย่งเคอร์เซอร์จากช่องพิมพ์คำตอบ
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = Math.min(h, 40);
        boolean pressed = getModel().isPressed();
        int top = pressed ? 2 : 0;

        g.setColor(GameConfig.DARK);
        g.fillRoundRect(0, top, w, h - top, arc, arc);
        Color fill = isEnabled() ? color : new Color(130, 130, 140);
        if (getModel().isRollover() && isEnabled()) {
            fill = fill.brighter();
        }
        g.setColor(fill);
        g.fillRoundRect(3, top + 3, w - 6, h - top - 9, arc - 6, arc - 6);

        Font font = getFont();
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int textX = (w - metrics.stringWidth(getText())) / 2;
        int textY = top + (h - 3 - metrics.getHeight()) / 2 + metrics.getAscent();
        g.setColor(textColor);
        g.drawString(getText(), textX, textY);
        g.dispose();
    }
}
