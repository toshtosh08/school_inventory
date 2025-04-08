import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteDataFrame extends JFrame {
    public DeleteDataFrame() {
        setTitle("Delete Data (Major Admin Only)");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color pinkRed = new Color(255, 105, 180);
        getContentPane().setBackground(pinkRed);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(pinkRed);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField();
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Admin", "Student", "Result"});
        JButton deleteButton = new JButton("Delete");

        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Type:"));
        panel.add(typeCombo);
        panel.add(new JLabel(""));
        panel.add(deleteButton);

        add(panel, BorderLayout.CENTER);

        deleteButton.addActionListener(e -> deleteData(idField.getText(), (String) typeCombo.getSelectedItem()));
    }

    private void deleteData(String id, String type) {
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an ID.");
            return;
        }

        try (Connection conn = Database.connect()) {
            String sql = "";
            switch (type) {
                case "Admin":
                    sql = "DELETE FROM admins WHERE id = ?";
                    break;
                case "Student":
                    sql = "DELETE FROM students WHERE id = ?";
                    break;
                case "Result":
                    sql = "DELETE FROM results WHERE id = ?";
                    break;
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(id));
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, type + " deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No " + type + " found with ID: " + id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.");
        }
    }
}