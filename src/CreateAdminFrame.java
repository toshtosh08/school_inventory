import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateAdminFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox isMajorAdminCheck;

    public CreateAdminFrame() {
        setTitle("Create New Admin");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color pinkRed = new Color(255, 105, 180);
        getContentPane().setBackground(pinkRed);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(pinkRed);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Corrected line

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        isMajorAdminCheck = new JCheckBox("Major Admin");
        JButton createButton = new JButton("Create");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(isMajorAdminCheck);
        panel.add(new JLabel(""));
        panel.add(createButton);

        add(panel, BorderLayout.CENTER);

        createButton.addActionListener(e -> createAdmin());
    }

    private void createAdmin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        boolean isMajorAdmin = isMajorAdminCheck.isSelected();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO admins (username, password, isMajorAdmin) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, isMajorAdmin ? 1 : 0);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Admin created successfully!");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}