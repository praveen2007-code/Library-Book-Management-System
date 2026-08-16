import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class BorrowBookPanel extends JPanel {
    private JTable table;

    public BorrowBookPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Borrowed Books Record", SwingConstants.CENTER);
        UIUtils.styleLabel(titleLabel, UIUtils.FONT_TITLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        table = new JTable();
        UIUtils.styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);
        
        loadRecords();
    }

    private void loadRecords() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                SELECT b.record_id AS borrow_id, 
                       u.username, 
                       bo.title, 
                       b.borrow_date, 
                       b.due_date, 
                       CASE WHEN b.return_date IS NULL THEN 'No' ELSE 'Yes' END AS returned
                FROM borrow_records b
                JOIN users u ON b.user_id = u.user_id
                JOIN books bo ON b.book_id = bo.book_id
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            java.util.Vector<String> cols = new java.util.Vector<>();
            cols.add("Borrow ID");
            cols.add("Username");
            cols.add("Book Title");
            cols.add("Borrow Date");
            cols.add("Due Date");
            cols.add("Returned");

            java.util.Vector<java.util.Vector<Object>> data = new java.util.Vector<>();
            while (rs.next()) {
                java.util.Vector<Object> row = new java.util.Vector<>();
                row.add(rs.getInt("borrow_id"));
                row.add(rs.getString("username"));
                row.add(rs.getString("title"));
                row.add(rs.getDate("borrow_date"));
                row.add(rs.getDate("due_date"));
                row.add(rs.getString("returned"));
                data.add(row);
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading records: " + ex.getMessage());
        }
    }
}
