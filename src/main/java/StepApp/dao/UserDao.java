package StepApp.dao;

import StepApp.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {

    HashMap<String, String> getUserPassword(String email);
    boolean addUser(User user);
    void createUserTable();
    void createLikedTable();
    int usersCount();
    String[] getTestData(String whichData);
    List<User> getUsers();
    void setUserEmailToCookie (String userEmail, HttpServletResponse resp);
    Optional<String> getUserEmailFromCookie(HttpServletRequest request);
    void addToLikedDb(String userEmail, String likedUser);
    List<String> getLikedUsersFromDb(String userEmail);
    void addEndLikedToCookie(HttpServletResponse response);
    Optional<String> getLikedEnd(HttpServletRequest request);
    Optional<User> getUserByEmail(String email);
    Optional<String> getUserEmailById(int id);
    void removeEndLikedFromCookie(HttpServletResponse response);
}
