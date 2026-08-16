import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;

public class AdminDashboard extends JFrame {

    private JButton addBookBtn, viewBorrowedBtn, sendReminderBtn;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UIUtils.COLOR_BACKGROUND);
        setLayout(new BorderLayout());

        // Header Title
        JLabel titleLabel = new JLabel("Library Admin Dashboard", SwingConstants.CENTER);
        UIUtils.styleLabel(titleLabel, UIUtils.FONT_TITLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        btnPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        // Buttons
        addBookBtn = new JButton("Add Book");
        viewBorrowedBtn = new JButton("View Borrowed Books");
        sendReminderBtn = new JButton("Send Reminder");

        UIUtils.styleButton(addBookBtn);
        UIUtils.styleButton(viewBorrowedBtn);
        UIUtils.styleButton(sendReminderBtn);

        // Add to UI
        btnPanel.add(addBookBtn);
        btnPanel.add(viewBorrowedBtn);
        btnPanel.add(sendReminderBtn);
        
        add(btnPanel, BorderLayout.CENTER);

        // ---- Button Actions ----

        // Open Add Book Panel
        addBookBtn.addActionListener(e -> {
            JFrame frame = new JFrame("Add Book");
            frame.add(new AddBookPanel());
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        // View Borrowed Books
        viewBorrowedBtn.addActionListener(e -> {
            JFrame frame = new JFrame("Borrowed Books");
            frame.add(new BorrowBookPanel());
            frame.setSize(700, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        // Send Reminder Action
        sendReminderBtn.addActionListener(e -> sendReminders());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void sendReminders() {
        try (Connection conn = DBConnection.getConnection()) {

            // ✅ FIXED: corrected column names (bo.book_id instead of bo.id)
            String sql = """
                SELECT bo.title, b.due_date
                FROM borrow_records b
                JOIN books bo ON b.book_id = bo.book_id
                WHERE b.return_date IS NULL
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {
                String bookTitle = rs.getString("title");
                LocalDate dueDate = rs.getDate("due_date").toLocalDate();

                // ⚠️ You said no due-date check needed, so we’ll always send:
                SMSSender.sendReminder("+917708758047", bookTitle, dueDate.toString());
                count++;
            }

            JOptionPane.showMessageDialog(this,
                    count > 0
                            ? "✅ Sent " + count + " reminder(s) successfully to +91 7708758047!"
                            : "No borrowed books found.",
                    "Reminders Sent",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error sending reminders: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboard::new);
    }
}
