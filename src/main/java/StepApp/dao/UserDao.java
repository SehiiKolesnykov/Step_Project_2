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
    int usersCount();
    String[] getTestData(String whichData);
    List<User> getUsers();
    void setUserEmailToCookie (String userEmail, HttpServletResponse resp);
    Optional<String> getUserEmailFromCookie(HttpServletRequest request);
    Optional<User> getUserByEmail(String email);
    Optional<String> getUserEmailById(int id);
    String getDefaultAvatar (String gender);
    String getDefaultProfession();
    void updateUserLastLogin(HttpServletRequest request);
}
