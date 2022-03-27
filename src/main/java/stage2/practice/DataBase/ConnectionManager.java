package stage2.practice.DataBase;

import java.sql.*;
import java.util.Properties;

public class ConnectionManager {
    private static final String pathToDB = "jdbc:h2:file:C:\\Users\\Ричард\\IdeaProjects\\stage2\\src\\main\\java\\stage2\\demo;" +
            "AUTO_SERVER=TRUE;OLD_INFORMATION_SCHEMA=TRUE;";
    private static Connection conn;

    public static Connection createConnection() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Properties prop = new Properties();
        prop.put("user", "sa");
        prop.put("password", "");
        try {
            conn = DriverManager.getConnection(pathToDB, prop);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Statement getStatement() throws SQLException {
        if (conn == null)
            try {
                return createConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    public static void closeConnection() throws SQLException {
        conn.close();
    }
}
