import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:tipAutomation.db";
    private static final String TABLE = "TipsJune2025";
    private static DBConnection instance;
    private Connection conn;

    private DBConnection() {
        try {
            conn = DriverManager.getConnection(URL);
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "name TEXT, hours REAL, share REAL, totalTips REAL, timestamp INTEGER)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) instance = new DBConnection();
        return instance;
    }

    public void save(String name, double hrs, double share, double totalTips) {
        String sql = "INSERT INTO " + TABLE + " (name,hours,share,totalTips,timestamp) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, hrs);
            ps.setDouble(3, share);
            ps.setDouble(4, totalTips);
            ps.setLong(5, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
