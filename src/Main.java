import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new GameFrame().setVisible(true);
            } catch (IllegalStateException e) {
                // ส่วนใหญ่เกิดจากรันนอกโฟลเดอร์โปรเจกต์ เลยหาโฟลเดอร์ assets ไม่เจอ
                JOptionPane.showMessageDialog(null, e.getMessage(), "เปิดเกมไม่ได้",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
