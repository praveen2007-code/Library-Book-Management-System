import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UIUtils {

    public static final Color COLOR_PRIMARY = new Color(41, 128, 185); // Blue
    public static final Color COLOR_PRIMARY_DARK = new Color(31, 97, 141);
    public static final Color COLOR_BACKGROUND = new Color(245, 247, 250); // Light Gray/Blue
    public static final Color COLOR_TEXT_DARK = new Color(44, 62, 80);
    public static final Color COLOR_TEXT_LIGHT = Color.WHITE;

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public static void styleButton(JButton button) {
        button.setFont(FONT_BOLD);
        button.setBackground(COLOR_PRIMARY);
        button.setForeground(COLOR_TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(COLOR_PRIMARY_DARK);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(COLOR_PRIMARY);
                }
            }
        });
    }

    public static void styleLabel(JLabel label, Font font) {
        label.setFont(font);
        label.setForeground(COLOR_TEXT_DARK);
    }

    public static void styleTextField(JTextField textField) {
        textField.setFont(FONT_REGULAR);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(30);
        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setBackground(COLOR_PRIMARY);
        table.getTableHeader().setForeground(COLOR_TEXT_LIGHT);
        table.setSelectionBackground(new Color(173, 216, 230)); // Light Blue
        table.setSelectionForeground(COLOR_TEXT_DARK);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
    }
}
