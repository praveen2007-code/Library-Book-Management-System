import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {
    private int currentUserId;

    public UserDashboard(int userId) {
        this.currentUserId = userId;

        setTitle("📚 Library User Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);

        // ====== Sidebar Panel ======
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(6, 1, 10, 15));
        sidePanel.setBackground(UIUtils.COLOR_PRIMARY_DARK);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        sidePanel.setPreferredSize(new Dimension(220, 0));

        JLabel menuLabel = new JLabel("Menu", SwingConstants.CENTER);
        menuLabel.setForeground(UIUtils.COLOR_TEXT_LIGHT);
        menuLabel.setFont(UIUtils.FONT_SUBTITLE);
        sidePanel.add(menuLabel);

        JButton borrowBookBtn = new JButton("Borrow Book");
        JButton searchBookBtn = new JButton("Search Book");
        JButton returnBookBtn = new JButton("Return Book");
        JButton logoutBtn = new JButton("Logout");

        // Style Sidebar Buttons
        JButton[] sidebarBtns = {borrowBookBtn, searchBookBtn, returnBookBtn, logoutBtn};
        for (JButton btn : sidebarBtns) {
            btn.setFont(UIUtils.FONT_BOLD);
            btn.setBackground(UIUtils.COLOR_PRIMARY);
            btn.setForeground(UIUtils.COLOR_TEXT_LIGHT);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(52, 152, 219)); // Lighter Blue
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(UIUtils.COLOR_PRIMARY);
                }
            });
            sidePanel.add(btn);
        }

        add(sidePanel, BorderLayout.WEST);

        // ====== Main Content Area ======
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        add(mainPanel, BorderLayout.CENTER);

        // ====== Button Actions ======
        borrowBookBtn.addActionListener(e -> {
            mainPanel.removeAll();
            mainPanel.add(new BorrowBookPanel(), BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        searchBookBtn.addActionListener(e -> {
            mainPanel.removeAll();
            mainPanel.add(new SearchBookPanel(currentUserId), BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        returnBookBtn.addActionListener(e -> {
            mainPanel.removeAll();
            mainPanel.add(new ReturnBookPanel(currentUserId), BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        // Default view
        borrowBookBtn.doClick();

        setVisible(true);
    }
}
