import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        // Window setup
        setTitle("Satris College - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Pink-red theme
        Color pinkRed = new Color(255, 105, 180);
        getContentPane().setBackground(pinkRed);

        // Layout
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(pinkRed);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Components
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        // Add to panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel("")); // Empty cell
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);

        // Login action
        loginButton.addActionListener(e -> login());
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            return;
        }

        try (Connection conn = Database.connect()) {
            String sql = "SELECT id, isMajorAdmin FROM admins WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Note: In production, hash passwords!
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int adminId = rs.getInt("id");
                boolean isMajorAdmin = rs.getInt("isMajorAdmin") == 1;
                JOptionPane.showMessageDialog(this, "Login successful!");
                new AdminDashboard(adminId, isMajorAdmin).setVisible(true);
                dispose(); // Close login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Login error: " + e.getMessage());
        }
    }
}