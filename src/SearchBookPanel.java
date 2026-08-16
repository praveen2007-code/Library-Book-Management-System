import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class SearchBookPanel extends JPanel {
    private JTextField searchField;
    private JTable resultTable;
    private int currentUserId;
    private JButton borrowButton;

    public SearchBookPanel(int userId) {
        this.currentUserId = userId;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(UIUtils.COLOR_BACKGROUND);

        // --- Header ---
        JLabel titleLabel = new JLabel("🔍 Search & Borrow Books", SwingConstants.CENTER);
        UIUtils.styleLabel(titleLabel, UIUtils.FONT_TITLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- Search Bar ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        topPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        
        searchField = new JTextField(25);
        UIUtils.styleTextField(searchField);
        
        JButton searchButton = new JButton("Search");
        UIUtils.styleButton(searchButton);
        
        borrowButton = new JButton("Borrow Selected Book");
        UIUtils.styleButton(borrowButton);
        borrowButton.setEnabled(false); // disabled until selection
        
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(borrowButton);
        add(topPanel, BorderLayout.SOUTH);

        // --- Table ---
        resultTable = new JTable();
        UIUtils.styleTable(resultTable);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // --- Events ---
        searchButton.addActionListener(e -> searchBooks());
        borrowButton.addActionListener(e -> borrowSelectedBook());
        resultTable.getSelectionModel().addListSelectionListener(e -> {
            borrowButton.setEnabled(resultTable.getSelectedRow() != -1);
        });
    }

    // ------------------------- SEARCH -------------------------
    private void searchBooks() {
        String term = searchField.getText().trim();
        if (term.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title or author to search.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT book_id, title, author, quantity FROM books WHERE title LIKE ? OR author LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            String likeTerm = "%" + term + "%";
            ps.setString(1, likeTerm);
            ps.setString(2, likeTerm);
            ResultSet rs = ps.executeQuery();

            java.util.Vector<String> cols = new java.util.Vector<>();
            cols.add("Book ID");
            cols.add("Title");
            cols.add("Author");
            cols.add("Available Qty");

            java.util.Vector<java.util.Vector<Object>> data = new java.util.Vector<>();
            while (rs.next()) {
                java.util.Vector<Object> row = new java.util.Vector<>();
                row.add(rs.getInt("book_id"));
                row.add(rs.getString("title"));
                row.add(rs.getString("author"));
                row.add(rs.getInt("quantity"));
                data.add(row);
            }

            resultTable.setModel(new javax.swing.table.DefaultTableModel(data, cols));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching books: " + ex.getMessage());
        }
    }

    // ------------------------- BORROW -------------------------
    private void borrowSelectedBook() {
        int row = resultTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book first.");
            return;
        }

        int bookId = (int) resultTable.getValueAt(row, 0);
        int available = (int) resultTable.getValueAt(row, 3);

        if (available <= 0) {
            JOptionPane.showMessageDialog(this, "Sorry, this book is not available.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1️⃣ Reduce quantity
            PreparedStatement ps1 = conn.prepareStatement("UPDATE books SET quantity = quantity - 1 WHERE book_id = ?");
            ps1.setInt(1, bookId);
            ps1.executeUpdate();

            // 2️⃣ Add record in borrow_records
            PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO borrow_records (user_id, book_id, borrow_date, due_date) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY))"
            );
            ps2.setInt(1, currentUserId);
            ps2.setInt(2, bookId);
            ps2.executeUpdate();

            conn.commit();

            JOptionPane.showMessageDialog(this, "✅ Book borrowed successfully!");
            searchBooks(); // refresh table
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error borrowing book: " + ex.getMessage());
        }
    }
}
