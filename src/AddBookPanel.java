import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddBookPanel extends JPanel {
    private JTextField titleField, authorField, qtyField;

    public AddBookPanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);

        JLabel titleLabel = new JLabel("Add New Book", SwingConstants.CENTER);
        UIUtils.styleLabel(titleLabel, UIUtils.FONT_TITLE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4,2,10,15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        titleField = new JTextField();
        authorField = new JTextField();
        qtyField = new JTextField();

        UIUtils.styleTextField(titleField);
        UIUtils.styleTextField(authorField);
        UIUtils.styleTextField(qtyField);

        JLabel lblTitle = new JLabel("Title:");
        JLabel lblAuthor = new JLabel("Author:");
        JLabel lblQty = new JLabel("Quantity:");
        
        UIUtils.styleLabel(lblTitle, UIUtils.FONT_BOLD);
        UIUtils.styleLabel(lblAuthor, UIUtils.FONT_BOLD);
        UIUtils.styleLabel(lblQty, UIUtils.FONT_BOLD);

        formPanel.add(lblTitle);
        formPanel.add(titleField);
        formPanel.add(lblAuthor);
        formPanel.add(authorField);
        formPanel.add(lblQty);
        formPanel.add(qtyField);

        JButton addBtn = new JButton("Add Book");
        UIUtils.styleButton(addBtn);
        
        // Add a placeholder label for spacing
        formPanel.add(new JLabel(""));
        formPanel.add(addBtn);

        add(formPanel, BorderLayout.CENTER);
        
        addBtn.addActionListener(e -> addBook());
    }

    private void addBook() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, titleField.getText());
            ps.setString(2, authorField.getText());
            ps.setInt(3, Integer.parseInt(qtyField.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book added successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
