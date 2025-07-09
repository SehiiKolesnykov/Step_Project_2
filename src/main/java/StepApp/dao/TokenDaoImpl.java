package StepApp.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

public class TokenDaoImpl implements TokenDao {

    AppDao appDao;
    public TokenDaoImpl() {
        this.appDao = new AppDaoImpl();
    }

    @Override
    public Optional<UUID> getTokenFromCookie(HttpServletRequest request) {
        return appDao.getCookieValue(request, "USER_TOKEN", UUID::fromString);
    }

    @Override
    public Optional<UUID> getTokenFromDB(String email) {
        String query = "SELECT token FROM tokens WHERE email = ?";
        return appDao.executeQuery(query,
                stmt -> stmt.setString(1, email),
                rs -> rs.next() ? Optional.of(UUID.fromString(rs.getString(1))) : Optional.empty());
    }

    @Override
    public Optional<String> checkToken(UUID token) {
        String getQuery = "SELECT email FROM tokens WHERE token = ?";
        return appDao.executeQuery(getQuery,
                stmt -> stmt.setString(1, token.toString()),
                rs -> rs.next() ? Optional.ofNullable(rs.getString(1)) : Optional.empty());
    }

    @Override
    public void setTokenToCookie(UUID token, HttpServletResponse resp) {
        appDao.setCookieValue(resp, "USER_TOKEN", token.toString());
    }

    @Override
    public void setTokenToDB(UUID token, String email) {
        String insertQuery =
                """
                        INSERT INTO tokens (email, token) VALUES (?, ?)
                        ON CONFLICT (email) DO UPDATE SET token = EXCLUDED.token
                """;

        boolean success = appDao.executeUpdate(insertQuery, stmt -> {
            stmt.setString(1, email);
            stmt.setString(2, token.toString());
        });
        if (success) {
            System.out.println("Token added successfully");
        } else {
            System.err.println("Token not added");
        }
    }

    @Override
    public void removeTokenFromCookie(HttpServletResponse resp) {
        appDao.removeCookie("USER_TOKEN", resp);
    }

    @Override
    public void removeTokenFromDB(UUID token) {
        String deleteQuery = "DELETE FROM tokens WHERE token = ?";
        boolean success = appDao.executeUpdate(deleteQuery,
                stmt -> stmt.setString(1, token.toString()));
        if (success) {
            System.out.println("Token deleted successfully");
        } else {
            System.err.println("Token not deleted");
        }
    }

    @Override
    public void createUserTable() {

        String creteTabTokensSql =
                """
                        CREATE TABLE IF NOT EXISTS tokens (
                        email       VARCHAR(255)
                                    CONSTRAINT tokens_pk PRIMARY KEY
                                    CONSTRAINT fk_email REFERENCES users(email),
                        token       TEXT NOT NULL
                                                         )
                """;

        appDao.createTable(creteTabTokensSql, "tokens");
    }
}
