package StepApp.controller;

import StepApp.model.User;
import StepApp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserController {

    UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    public boolean checkPassword(String email, String password) {
        return userService.checkPassword(email, password);
    }

    public boolean registerUser (User user) {
        return userService.registerUser(user);
    }

    public boolean startApp() {
        return userService.startApp();
    }

    public void setUserEmailToCookie (String userEmail, HttpServletResponse resp) {
        userService.setUserEmailToCookie(userEmail, resp);
    }

    public Optional<String> getUserEmailFromCookie(HttpServletRequest request) {
        return userService.getUserEmailFromCookie(request);
    }

    public Map<String, Object> likePageData (HttpServletRequest request, HttpServletResponse response) {
        return userService.likePageData(request, response);
    }

    public List<Map<String, Object>> getLikedUsersData (HttpServletRequest request) {
        return userService.getLickedUsersData(request);
    }

    public Map<String, Object> getUserDataByEmail(String email) {
        return userService.getUserDataByEmail(email);
    }

    public Optional<String> getUserEmailById(int id) {
        return userService.getUserEmailById(id);
    }

    public void removeEndLikedFromCookie(HttpServletResponse response) {
        userService.removeEndLikedFromCookie(response);
    }

}
