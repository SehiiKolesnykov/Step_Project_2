package StepApp.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public static Connection conn() throws SQLException {
       try {
           String url = System.getenv("DATABASE_URL") != null ? System.getenv("DATABASE_URL") : "jdbc:postgresql://localhost:5432/step_app";
           String username = System.getenv("DB_USERNAME") != null ? System.getenv("DB_USERNAME") : "postgres";
           String password = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "<PASSWORD>";
           return DriverManager.getConnection(url, username, password);
       } catch (SQLException e) {
           throw new SQLException("Error connecting to the database", e);
       }
    }
}
