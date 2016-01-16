import java.sql.Connection;
import java.sql.DriverManager;


public final class DB {
    public final static Connection createConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.204.128:1433", "sa","1");
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=DENEMELER", "sa", "1");
            return conn;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}