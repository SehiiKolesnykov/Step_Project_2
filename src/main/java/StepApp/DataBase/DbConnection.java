package StepApp.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    static String url = "jdbc:postgresql://localhost:5432/tinder";
    static String username = "postgres";
    static String password = "pg123456";

    public static Connection conn() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
