package StepApp.dao;

import StepApp.dataBase.DbConnection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.Arrays;
import java.util.Optional;

public class AppDaoImpl implements AppDao{
    @Override
    public Connection getConnection() throws SQLException {
        return DbConnection.conn();
    }

    @Override
    public <T> T executeQuery(String query, PreparedStatementSetter setter, ResultSetHandler<T> handler) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                setter.setParameters(stmt);
                ResultSet rs = stmt.executeQuery();
                return handler.handle(rs);
            }
        } catch (SQLException e) {
            System.err.println("Помилка запиту: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean executeUpdate(String query, PreparedStatementSetter setter) {
        try (Connection conn = getConnection()){
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                setter.setParameters(stmt);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Помилка виконання: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void createTable(String createTableQuery, String tableName) {
        try (Connection conn = getConnection()){
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTableQuery);
                System.out.println("Таблицю " + tableName + " успішно створено");
            }
        } catch (SQLException e) {
            System.err.println("Помилка створення таблиці " + tableName + ": " + e.getMessage());
        }
    }

    @Override
    public <T> Optional<T> getCookieValue(HttpServletRequest request, String cookieName, CookieValueMapper<T> mapper) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .map(mapper::map);
    }

    @Override
    public void setCookieValue(HttpServletResponse resp, String cookieName, String cookieValue) {
        Cookie c = new Cookie(cookieName, cookieValue);
        c.setPath("/");
        resp.addCookie(c);
    }

    @Override
    public void removeCookie(String cookieName, HttpServletResponse resp) {
        Cookie c = new Cookie(cookieName, "");
        c.setPath("/");
        c.setMaxAge(0);
        resp.addCookie(c);
    }
}
