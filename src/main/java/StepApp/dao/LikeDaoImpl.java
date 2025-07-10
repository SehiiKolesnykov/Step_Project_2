package StepApp.dao;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LikeDaoImpl implements LikeDao {

    AppDao appDao;

    public LikeDaoImpl() {
        this.appDao = new AppDaoImpl();
    }


    @Override
    public void createLikedTable() {
        String creteTabLikedSql = """
                 CREATE TABLE IF NOT EXISTS liked (
                 userEmail        varchar(255),
                 liked           varchar(255)
                 )
                """;

        appDao.createTable(creteTabLikedSql, "liked");
    }

    @Override
    public void createRatedTable() {
        String createTableSql = """
                CREATE TABLE IF NOT EXISTS rated (
                userEmail        varchar(255),
                rated           varchar(255)
                )
                """;
        appDao.createTable(createTableSql, "rated");
    }

    @Override
    public void addToLikedDb(String userEmail, String likedUser) {
        String insertQuery = """
                INSERT INTO liked (useremail, liked)
                VALUES (?, ?)
                """;

        boolean success = appDao.executeUpdate(insertQuery, stmt -> {
            stmt.setString(1, userEmail);
            stmt.setString(2, likedUser);
        });
    }

    @Override
    public void addToRatedDb(String userEmail, String ratedUser) {
        String insertQuery = """
                INSERT INTO rated (useremail, rated)
                VALUES (?, ?)
                """;

        boolean success = appDao.executeUpdate(insertQuery, stmt -> {
            stmt.setString(1, userEmail);
            stmt.setString(2, ratedUser);
        });
    }

    @Override
    public List<String> getLikedUsersFromDb(String userEmail) {
        String getQuery = "SELECT liked FROM liked WHERE useremail = ?";
        return appDao.executeQuery(getQuery, stmt -> stmt.setString(1, userEmail), rs -> {
            List<String> lickedUsers = new ArrayList<>();
            while (rs.next()) {
                lickedUsers.add(rs.getString("liked"));
            }
            return lickedUsers;
        });
    }

    @Override
    public List<String> getRatedUsersFromDb(String userEmail) {
        String getQuery = "SELECT rated FROM rated WHERE useremail = ?";
        return appDao.executeQuery(getQuery, stmt -> stmt.setString(1, userEmail), rs -> {
            List<String> ratedUsers = new ArrayList<>();
            while (rs.next()) {
                ratedUsers.add(rs.getString("rated"));
            }
            return ratedUsers;
        });
    }

    @Override
    public void removeFromRatedDb(String userEmail) {
        String deleteQuery = "DELETE FROM rated WHERE useremail = ?";
        boolean success = appDao.executeUpdate(deleteQuery, stmt -> {
            stmt.setString(1, userEmail);
        });
    }

    @Override
    public void addEndLikedToCookie(HttpServletResponse response) {
        Cookie endCookie = new Cookie("END", "true");
        endCookie.setPath("/");
        endCookie.setMaxAge(3600);
        response.addCookie(endCookie);
    }

    @Override
    public Optional<String> getLikedEnd(HttpServletRequest request) {
        return appDao.getCookieValue(request, "END", value -> value);
    }

    @Override
    public void removeEndLikedFromCookie(HttpServletResponse response) {
        appDao.removeCookie("END", response);
    }
}
