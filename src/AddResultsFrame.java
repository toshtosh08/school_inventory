import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddResultsFrame extends JFrame {
    public AddResultsFrame() {
        setTitle("Add Student Results");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color pinkRed = new Color(255, 105, 180);
        getContentPane().setBackground(pinkRed);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(pinkRed);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField studentIdField = new JTextField();
        JTextField subjectField = new JTextField();
        JTextField gradeField = new JTextField();
        JButton addButton = new JButton("Add Result");

        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdField);
        panel.add(new JLabel("Subject:"));
        panel.add(subjectField);
        panel.add(new JLabel("Grade:"));
        panel.add(gradeField);
        panel.add(new JLabel(""));
        panel.add(addButton);

        add(panel, BorderLayout.CENTER);

        addButton.addActionListener(e -> addResult(
                studentIdField.getText(), subjectField.getText(), gradeField.getText()));
    }

    private void addResult(String studentId, String subject, String grade) {
        if (studentId.isEmpty() || subject.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO results (studentId, subject, grade) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(studentId));
            pstmt.setString(2, subject);
            pstmt.setString(3, grade);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Result added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID format.");
        }
    }
}