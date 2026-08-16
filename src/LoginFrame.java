import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Library Login");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        // Title
        JLabel titleLabel = new JLabel("Library System Login", SwingConstants.CENTER);
        UIUtils.styleLabel(titleLabel, UIUtils.FONT_TITLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        formPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JLabel userLabel = new JLabel("Username:");
        UIUtils.styleLabel(userLabel, UIUtils.FONT_BOLD);
        usernameField = new JTextField();
        UIUtils.styleTextField(usernameField);
        formPanel.add(userLabel);
        formPanel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        UIUtils.styleLabel(passLabel, UIUtils.FONT_BOLD);
        passwordField = new JPasswordField();
        UIUtils.styleTextField(passwordField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        JButton loginButton = new JButton("Login to System");
        UIUtils.styleButton(loginButton);
        btnPanel.add(loginButton);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> doLogin());

        add(mainPanel);
        setVisible(true);
    }

    private void doLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed. Check DB settings.");
                return;
            }

            String sql = "SELECT user_id, role, phone FROM users WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String role = rs.getString("role");

                if ("admin".equalsIgnoreCase(role)) {
                    new AdminDashboard();
                } else {
                    new UserDashboard(userId);  // ✅ FIXED HERE
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login: " + ex.getMessage());
        }
    }
}
