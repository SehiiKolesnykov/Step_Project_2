package StepApp.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface AppDao {

    Connection getConnection() throws SQLException;
    <T> T executeQuery(String query, PreparedStatementSetter setter, ResultSetHandler<T> handler);
    boolean executeUpdate(String query, PreparedStatementSetter setter);
    void createTable(String createTableQuery, String tableName);
    <T>Optional<T> getCookieValue(HttpServletRequest request, String cookieName, CookieValueMapper<T> mapper);
    void setCookieValue(HttpServletResponse resp, String cookieName, String cookieValue);
    void removeCookie(String cookieName, HttpServletResponse resp);


    @FunctionalInterface
    interface PreparedStatementSetter {
        void setParameters(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }

    @FunctionalInterface
    interface CookieValueMapper<T> {
        T map(String value);
    }

}
