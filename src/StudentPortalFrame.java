import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentPortalFrame extends JFrame {
    private boolean isMajorAdmin;
    private JTextField nameField;
    private JTextField idField;
    private JTextField birthCertPathField;
    private JTextField photoPathField;
    private JTextField feeField;

    public StudentPortalFrame(boolean isMajorAdmin) {
        this.isMajorAdmin = isMajorAdmin;
        setTitle("Satris College - Student Portal");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color pinkRed = new Color(255, 105, 180);
        Color lightPink = new Color(255, 182, 193);
        getContentPane().setBackground(pinkRed);

        // Main panel with GridBagLayout for vertical alignment
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(pinkRed);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left

        // Initialize fields
        nameField = new JTextField(20);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        idField = new JTextField(20);
        idField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        birthCertPathField = new JTextField(20);
        birthCertPathField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        birthCertPathField.setEditable(false);
        JButton birthCertButton = new JButton("Choose Birth Certificate");
        styleButton(birthCertButton, lightPink);
        photoPathField = new JTextField(20);
        photoPathField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        photoPathField.setEditable(false);
        JButton photoButton = new JButton("Choose Photo");
        styleButton(photoButton, lightPink);
        feeField = new JTextField(20);
        feeField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JButton addButton = new JButton("Add Student");
        styleButton(addButton, lightPink);

        // Add components vertically
        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);

        // Student ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(idField, gbc);

        // Birth Certificate
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Birth Certificate:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(birthCertPathField, gbc);

        // Birth Certificate Button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(birthCertButton, gbc);

        // Passport Photo
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Passport Photo (Optional):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(photoPathField, gbc);

        // Passport Photo Button
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(photoButton, gbc);

        // Fee Balance
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Fee Balance:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(feeField, gbc);

        // Add Button
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST; // Align to the right
        panel.add(addButton, gbc);

        // Back button
        JButton backButton = new JButton("Back to Home");
        styleButton(backButton, lightPink);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(pinkRed);
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);

        // Action listeners
        birthCertButton.addActionListener(e -> chooseFile(birthCertPathField));
        photoButton.addActionListener(e -> chooseFile(photoPathField));
        addButton.addActionListener(e -> addStudent(
                nameField.getText(), idField.getText(), birthCertPathField.getText(),
                photoPathField.getText(), feeField.getText()));
        backButton.addActionListener(e -> dispose());
    }

    // Helper method to style buttons
    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        button.setFocusPainted(false);
    }

    // Method to open file chooser and select an image
    private void chooseFile(JTextField targetField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            targetField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void addStudent(String name, String id, String birthCert, String photo, String fee) {
        if (name.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Student ID are required.");
            return;
        }

        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO students (name, studentId, birthCert, photoPath, feeBalance) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, id);
            pstmt.setString(3, birthCert.isEmpty() ? null : birthCert);
            pstmt.setString(4, photo.isEmpty() ? null : photo);
            pstmt.setDouble(5, fee.isEmpty() ? 0.0 : Double.parseDouble(fee));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            nameField.setText("");
            idField.setText("");
            birthCertPathField.setText("");
            photoPathField.setText("");
            feeField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid fee balance format.");
        }
    }
}