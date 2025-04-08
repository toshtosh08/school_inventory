import javax.swing.*;

public class SatrisCollegeApp {
    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}