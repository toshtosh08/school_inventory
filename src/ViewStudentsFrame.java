import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewStudentsFrame extends JFrame {
    private boolean isMajorAdmin;

    public ViewStudentsFrame(boolean isMajorAdmin) {
        this.isMajorAdmin = isMajorAdmin;
        setTitle("Satris College - View All Students");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color pinkRed = new Color(255, 105, 180);
        Color lightPink = new Color(255, 182, 193);
        getContentPane().setBackground(pinkRed);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(pinkRed);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea studentsArea = new JTextArea(15, 50);
        studentsArea.setEditable(false);
        studentsArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton backButton = new JButton("Back to Home");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setBackground(lightPink);
        backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        backButton.setFocusPainted(false);

        panel.add(new JScrollPane(studentsArea), BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        add(panel);

        loadStudents(studentsArea);

        backButton.addActionListener(e -> dispose());
    }

    private void loadStudents(JTextArea studentsArea) {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT * FROM students";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id"))
                        .append(", Name: ").append(rs.getString("name"))
                        .append(", Student ID: ").append(rs.getString("studentId"))
                        .append(", Birth Cert: ").append(rs.getString("birthCert") != null ? rs.getString("birthCert") : "N/A")
                        .append(", Photo: ").append(rs.getString("photoPath") != null ? rs.getString("photoPath") : "N/A")
                        .append(", Fee Balance: ksh").append(rs.getDouble("feeBalance"))
                        .append("\n--------------------------------------------------\n");
            }
            if (sb.length() == 0) {
                sb.append("No students found.");
            }
            studentsArea.setText(sb.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
        }
    }
}