import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintRecordsFrame extends JFrame implements Printable {
    private JTextField studentIdField;
    private JTextArea printArea;

    public PrintRecordsFrame() {
        setTitle("Print Student Records");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color pinkRed = new Color(255, 105, 180);
        getContentPane().setBackground(pinkRed);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(pinkRed);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        studentIdField = new JTextField(10);
        JButton fetchButton = new JButton("Fetch Records");
        printArea = new JTextArea(10, 30);
        JButton printButton = new JButton("Print");

        JPanel topPanel = new JPanel();
        topPanel.setBackground(pinkRed);
        topPanel.add(new JLabel("Student ID:"));
        topPanel.add(studentIdField);
        topPanel.add(fetchButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(printArea), BorderLayout.CENTER);
        panel.add(printButton, BorderLayout.SOUTH);

        add(panel);

        fetchButton.addActionListener(e -> fetchRecords());
        printButton.addActionListener(e -> printRecords());
    }

    private void fetchRecords() {
        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a Student ID.");
            return;
        }

        try (Connection conn = Database.connect()) {
            String sql = "SELECT s.name, s.studentId, s.feeBalance, r.subject, r.grade " +
                    "FROM students s LEFT JOIN results r ON s.id = r.studentId " +
                    "WHERE s.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(studentId));
            ResultSet rs = pstmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            if (rs.next()) {
                sb.append("Name: ").append(rs.getString("name")).append("\n");
                sb.append("Student ID: ").append(rs.getString("studentId")).append("\n");
                sb.append("Fee Balance: ksh").append(rs.getDouble("feeBalance")).append("\n");
                sb.append("Results:\n");
                do {
                    if (rs.getString("subject") != null) {
                        sb.append("  ").append(rs.getString("subject"))
                                .append(": ").append(rs.getString("grade")).append("\n");
                    }
                } while (rs.next());
            } else {
                sb.append("No records found for Student ID: ").append(studentId);
            }
            printArea.setText(sb.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void printRecords() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Printing error: " + e.getMessage());
            }
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        if (page > 0) return NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        printArea.print(g2d); // Print the text area content
        return PAGE_EXISTS;
    }
}