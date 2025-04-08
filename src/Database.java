import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:./satris_college.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            createTables(conn);
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
        return conn;
    }

    private static void createTables(Connection conn) throws SQLException {
        String adminTable = "CREATE TABLE IF NOT EXISTS admins (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "isMajorAdmin INTEGER DEFAULT 0)";

        String studentTable = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "studentId TEXT NOT NULL UNIQUE, " +
                "birthCert TEXT, " +
                "photoPath TEXT, " +
                "feeBalance REAL DEFAULT 0.0)";

        String resultsTable = "CREATE TABLE IF NOT EXISTS results (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "studentId INTEGER, " +
                "subject TEXT NOT NULL, " +
                "grade TEXT NOT NULL, " +
                "FOREIGN KEY(studentId) REFERENCES students(id))";

        Statement stmt = conn.createStatement();
        stmt.execute(adminTable);
        stmt.execute(studentTable);
        stmt.execute(resultsTable);
        initializeDefaultAdmin(conn); // Call to insert default admin
        stmt.close();
    }

    private static void initializeDefaultAdmin(Connection conn) throws SQLException {
        String checkAdmin = "SELECT COUNT(*) FROM admins";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(checkAdmin);
        if (rs.getInt(1) == 0) {
            String insertAdmin = "INSERT INTO admins (username, password, isMajorAdmin) " +
                    "VALUES ('majoradmin', 'admin123', 1)";
            stmt.executeUpdate(insertAdmin);
            System.out.println("Default Major Admin created: majoradmin/admin123");
        }
        stmt.close();
    }
}