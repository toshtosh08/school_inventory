import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private int adminId;
    private boolean isMajorAdmin;

    public AdminDashboard(int adminId, boolean isMajorAdmin) {
        this.adminId = adminId;
        this.isMajorAdmin = isMajorAdmin;

        // Window setup
        setTitle("Satris College - Admin Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Modern Pink-red theme
        Color pinkRed = new Color(255, 105, 180);
        Color lightPink = new Color(255, 182, 193); // Lighter shade for buttons
        getContentPane().setBackground(pinkRed);

        // Panel with padding and layout
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10)); // Increased rows for new button
        panel.setBackground(pinkRed);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Buttons with modern styling
        JButton createAdminButton = new JButton("Create New Admin");
        styleButton(createAdminButton, lightPink);

        JButton manageStudentsButton = new JButton("Manage Students");
        styleButton(manageStudentsButton, lightPink);

        JButton viewStudentsButton = new JButton("View All Students"); // New button
        styleButton(viewStudentsButton, lightPink);

        JButton addResultsButton = new JButton("Add Results");
        styleButton(addResultsButton, lightPink);

        JButton printRecordsButton = new JButton("Print Records");
        styleButton(printRecordsButton, lightPink);

        JButton deleteDataButton = new JButton("Delete Data (Major Admin Only)");
        styleButton(deleteDataButton, lightPink);

        // Add buttons to panel
        panel.add(createAdminButton);
        panel.add(manageStudentsButton);
        panel.add(viewStudentsButton); // Added new button
        panel.add(addResultsButton);
        panel.add(printRecordsButton);
        if (isMajorAdmin) {
            panel.add(deleteDataButton); // Only visible to Major Admin
        }

        add(panel, BorderLayout.CENTER);

        // Action listeners
        createAdminButton.addActionListener(e -> openCreateAdmin());
        manageStudentsButton.addActionListener(e -> openStudentPortal());
        viewStudentsButton.addActionListener(e -> openViewStudents()); // New action
        addResultsButton.addActionListener(e -> openAddResults());
        printRecordsButton.addActionListener(e -> openPrintRecords());
        deleteDataButton.addActionListener(e -> openDeleteData());
    }

    // Helper method to style buttons
    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true)); // Rounded border
        button.setFocusPainted(false);
    }

    private void openCreateAdmin() {
        if (isMajorAdmin) {
            new CreateAdminFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Only Major Admin can create admins!");
        }
    }

    private void openStudentPortal() {
        new StudentPortalFrame(isMajorAdmin).setVisible(true);
    }

    private void openViewStudents() {
        new ViewStudentsFrame(isMajorAdmin).setVisible(true); // Opens the new view students page
    }

    private void openAddResults() {
        new AddResultsFrame().setVisible(true);
    }

    private void openPrintRecords() {
        new PrintRecordsFrame().setVisible(true);
    }

    private void openDeleteData() {
        if (isMajorAdmin) {
            new DeleteDataFrame().setVisible(true);
        }
    }
}