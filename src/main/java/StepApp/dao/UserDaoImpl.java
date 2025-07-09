package StepApp.dao;

import StepApp.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class UserDaoImpl implements UserDao {

    AppDao appDao;

    public UserDaoImpl() {
        this.appDao = new AppDaoImpl();
    }

    private static final String[] maleNames = {"Jack", "William", "Harry", "Jeff", "Tom"};
    private static final String[] femaleNames = {"Anna", "Emma", "Charlotte", "Sophia", "Alice"};
    private static final String[] surnames = {"Potter", "Smith", "Jackson", "Williams", "Brown"};
    private static final String[] professions = {"Teacher", "Doctor", "Engineer", "Architect", "Manager"};

    private static final String[] maleAvatars = {
            "https://hochu.ua/static/content/thumbs/780x468/0/f3/jjp65b---c1080x648x0sx42s--1cdd75c4ae51b51c6ccab1fff00bff30.jpg",
            "https://images.1plus1.ua/uploads/articles/000/406/590/7eb0f826ac11a8a421a99a06cab9c863.jpg",
            "https://www.rbc.ua/static/img/f/r/freepik_com_23467_7882d7c2360458a5183d81ec8ddc6ae0_1200x675.jpg",
            "https://s1.tchkcdn.com/g-kc1XuQbBDmLJ5NrXXukgXw/11/331986/660x480/c/0/0_125_1254_912/04336902247a51176ae8a5e1ffd3bde7_10257775_661023730691740_1235764939725580519_o.jpg",
            "https://st.depositphotos.com/1075946/3525/i/450/depositphotos_35250757-stock-photo-handsome-man-sitting-at-the.jpg"

    };

    private static final String[] femaleAvatars = {
            "https://static.wixstatic.com/media/0712e7_4b04755f5a364ec588e5616dbea68e43~mv2.jpg/v1/fill/w_480,h_484,al_c,q_80,usm_0.66_1.00_0.01,enc_avif,quality_auto/0712e7_4b04755f5a364ec588e5616dbea68e43~mv2.jpg",
            "https://rivne1.tv/pics2/1902/i102207.jpg",
            "https://st4.depositphotos.com/2056297/30838/i/450/depositphotos_308383034-stock-photo-portrait-seductive-blonde-woman-looking.jpg",
            "https://st.depositphotos.com/1006753/5098/i/450/depositphotos_50985571-stock-photo-young-stylish-woman-in-a.jpg",
            "https://www.rbc.ua/static/img/2/1/2149882239_02c4e0c601c785de2698acf5815ab328_1200x675.jpg"
    };

    @Override
    public HashMap<String, String> getUserPassword(String email) {
        String getQuery = "SELECT password FROM users WHERE email = ?";
        return appDao.executeQuery(getQuery,
                stmt -> stmt.setString(1, email),
                rs -> {
                    HashMap<String, String> password = new HashMap<>();
                    if (rs.next()) {
                        password.put(email, rs.getString(1));
                    }
                    return password;
                });
    }

    @Override
    public boolean addUser(User user) {
        String insertQuery =
                """
                        INSERT INTO users (name, surname, gender, avatarUrl, profession, email, password, lastvisit)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        """;

        boolean success = appDao.executeUpdate(insertQuery, stmt -> {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getGender());
            stmt.setString(4, user.getAvatarUrl());
            stmt.setString(5, user.getProfession());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getPassword());
            stmt.setLong(8, user.getLastVisit());
        });
        if (success) {
            System.out.println("User added successfully");
        }
        return success;
    }

    @Override
    public void createUserTable() {
        String createTabUsersSql =
                                """
                                CREATE TABLE IF NOT EXISTS users (
                                id          SERIAL,
                                name        VARCHAR(255),
                                surname     VARCHAR(255),
                                gender      VARCHAR(50),
                                avatarUrl   TEXT,
                                profession  VARCHAR(255),
                                email       VARCHAR(255) PRIMARY KEY,
                                password    VARCHAR(255),
                                lastVisit   BIGINT)
                                """;
        appDao.createTable(createTabUsersSql, "users");
    }

    @Override
    public void createLikedTable() {
        String creteTabLikedSql =
                """
                                CREATE TABLE IF NOT EXISTS liked (
                                userEmail        varchar(255),
                                liked           varchar(255)
                                                                 )
                        """;

        appDao.createTable(creteTabLikedSql, "liked");
    }


    @Override
    public int usersCount() {
        String getQuery = "SELECT COUNT(email) FROM users";
        Integer count = appDao.executeQuery(getQuery,
                stmt -> {
                },
                rs -> rs.next() ? rs.getInt(1) : 0);
        return count != null ? count : 0;
    }

    @Override
    public String[] getTestData(String whichData) {
        return switch (whichData) {
            case "maleAvatars" -> maleAvatars;
            case "maleNames" -> maleNames;
            case "femaleAvatars" -> femaleAvatars;
            case "femaleNames" -> femaleNames;
            case "surnames" -> surnames;
            case "professions" -> professions;
            default -> null;
        };
    }

    @Override
    public List<User> getUsers() {
        String getQuery = "SELECT id, name, surname, avatarurl, email, gender, profession, lastvisit FROM users";
        List<User> userList = appDao.executeQuery(getQuery,
                stmt -> {
                },
                rs -> {
                    List<User> users = new ArrayList<>();
                    while (rs.next()) {
                        users.add(new User(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("surname"),
                                rs.getString("gender"),
                                rs.getString("avatarurl"),
                                rs.getString("profession"),
                                rs.getLong("lastvisit"),
                                rs.getString("email")
                        ));
                    }
                    return users;
                });
        return userList != null ? userList : new ArrayList<>();
    }

    @Override
    public void setUserEmailToCookie(String userEmail, HttpServletResponse resp) {
        appDao.setCookieValue(resp, "USER_EMAIL", userEmail);
    }

    @Override
    public Optional<String> getUserEmailFromCookie(HttpServletRequest request) {
        return appDao.getCookieValue(request, "USER_EMAIL", value -> value);
    }

    @Override
    public void addToLikedDb(String userEmail, String likedUser) {
        String insertQuery =
                """
                        INSERT INTO liked (useremail, liked)
                        VALUES (?, ?)
                        """;

        boolean success = appDao.executeUpdate(insertQuery, stmt -> {
            stmt.setString(1, userEmail);
            stmt.setString(2, likedUser);
        });
        if (success) {
            System.out.println("Liked user added successfully");
        }
    }

    @Override
    public List<String> getLikedUsersFromDb(String userEmail) {
        String getQuery = "SELECT liked FROM liked WHERE useremail = ?";
        return appDao.executeQuery(getQuery,
                stmt -> stmt.setString(1, userEmail),
                rs -> {
                    List<String> lickedUsers = new ArrayList<>();
                    while (rs.next()) {
                        lickedUsers.add(rs.getString("liked"));
                    }
                    return lickedUsers;
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
    public Optional<User> getUserByEmail(String email) {
        String getQuery =
                        """
                        SELECT id, name, surname, avatarurl, email, gender, profession, lastvisit 
                        FROM users WHERE email = ?
                        """;
        return appDao.executeQuery(getQuery,
                stmt -> stmt.setString(1, email),
                rs -> rs.next() ? Optional.of(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("gender"),
                        rs.getString("avatarurl"),
                        rs.getString("profession"),
                        rs.getLong("lastvisit"),
                        rs.getString("email")
                )) : Optional.empty()
                );

    }

    @Override
    public Optional<String> getUserEmailById(int id) {
        String getQuery = "SELECT email FROM users WHERE id = ?";
        return appDao.executeQuery(getQuery,
                stmt -> stmt.setInt(1, id),
                rs -> rs.next() ? Optional.of(rs.getString("email")) : Optional.empty());
    }

    @Override
    public void removeEndLikedFromCookie(HttpServletResponse response) {
        appDao.removeCookie("END", response);
    }

//    // Узагальнений метод для логування результатів
//    private void logResult(boolean success, String successMessage, String errorMessage) {
//        if (success) {
//            System.out.println(successMessage);
//        } else {
//            System.err.println(errorMessage);
//        }
//    }
}
