import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class ReturnBookPanel extends JPanel {

    private JTextField bookIdField;
    private JButton returnButton;
    private JTable borrowedTable;
    private int currentUserId;

    public ReturnBookPanel(int userId) {
        this.currentUserId = userId;

        setLayout(new BorderLayout(10, 10));
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📘 Return Borrowed Book", SwingConstants.CENTER);
        UIUtils.styleLabel(titleLabel, UIUtils.FONT_TITLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table to show borrowed books
        borrowedTable = new JTable();
        UIUtils.styleTable(borrowedTable);
        JScrollPane scrollPane = new JScrollPane(borrowedTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // Panel for input
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel bookIdLabel = new JLabel("Enter Book ID:");
        UIUtils.styleLabel(bookIdLabel, UIUtils.FONT_BOLD);
        bottomPanel.add(bookIdLabel);
        
        bookIdField = new JTextField(15);
        UIUtils.styleTextField(bookIdField);
        bottomPanel.add(bookIdField);

        returnButton = new JButton("Return Book");
        UIUtils.styleButton(returnButton);
        bottomPanel.add(returnButton);

        add(bottomPanel, BorderLayout.SOUTH);

        loadBorrowedBooks();

        returnButton.addActionListener(e -> returnBook());
    }

    private void loadBorrowedBooks() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT b.book_id, b.title, br.borrow_date, br.due_date " +
                           "FROM borrow_records br " +
                           "JOIN books b ON br.book_id = b.book_id " +
                           "WHERE br.user_id = ? AND br.return_date IS NULL";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            borrowedTable.setModel(buildTableModel(rs));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error loading borrowed books.");
        }
    }

    private void returnBook() {
        String bookIdText = bookIdField.getText().trim();
        if (bookIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter Book ID.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            int bookId = Integer.parseInt(bookIdText);

            // Check if user has borrowed this book
            PreparedStatement check = conn.prepareStatement(
                "SELECT * FROM borrow_records WHERE user_id = ? AND book_id = ? AND return_date IS NULL");
            check.setInt(1, currentUserId);
            check.setInt(2, bookId);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "⚠️ You have not borrowed this book or already returned it.");
                return;
            }

            // Mark as returned (set return_date to today)
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE borrow_records SET return_date = CURDATE() WHERE user_id = ? AND book_id = ?");
            ps.setInt(1, currentUserId);
            ps.setInt(2, bookId);
            ps.executeUpdate();

            // Increase quantity in books
            PreparedStatement updateQty = conn.prepareStatement(
                "UPDATE books SET quantity = quantity + 1 WHERE book_id = ?");
            updateQty.setInt(1, bookId);
            updateQty.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Book returned successfully!");
            loadBorrowedBooks();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error while returning the book.");
        }
    }

    // Utility to build JTable model from ResultSet
    public static javax.swing.table.TableModel buildTableModel(ResultSet rs) throws SQLException {
        java.sql.ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        java.util.Vector<String> columnNames = new java.util.Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        java.util.Vector<java.util.Vector<Object>> data = new java.util.Vector<>();
        while (rs.next()) {
            java.util.Vector<Object> row = new java.util.Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        return new javax.swing.table.DefaultTableModel(data, columnNames);
    }
}
