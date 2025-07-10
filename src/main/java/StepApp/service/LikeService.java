package StepApp.service;

import StepApp.controller.UserController;
import StepApp.dao.LikeDao;
import StepApp.dao.LikeDaoImpl;
import StepApp.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class LikeService {

    LikeDao likeDao;
    UserController userController;

    public LikeService() {
        this.likeDao = new LikeDaoImpl();
        this.userController = new UserController();
    }

    public Map<String, Object> likePageData (HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<>();
        Optional<String> currentUserEmail = userController.getUserEmailFromCookie(request);

        Optional<String> endCookie = likeDao.getLikedEnd(request);
        if (endCookie.isPresent()) {
            data.put("redirect", "/liked");
            data.put("avatarUrl", "");
            data.put("userName", "");
            data.put("userSurname", "");
            data.put("userEmail", "");
            return data;
        }

        String action = request.getParameter("action");
        String ratedUserEmail = request.getParameter("userEmail");

        if ("like".equals(action) && ratedUserEmail != null) {
            currentUserEmail.ifPresent(email -> {
                likeDao.addToLikedDb(email, ratedUserEmail);
                likeDao.addToRatedDb(email, ratedUserEmail);
            });
        } else if ("dislike".equals(action) && ratedUserEmail != null) {
            likeDao.addToRatedDb(currentUserEmail.orElse(""), ratedUserEmail);
        }

        Optional<User> nextUser = getNextUser(currentUserEmail.orElse(""), request);
        if(nextUser.isPresent()){
            data.put("avatarUrl", nextUser.get().getAvatarUrl());
            data.put("userName", nextUser.get().getName());
            data.put("userSurname", nextUser.get().getSurname());
            data.put("userEmail", nextUser.get().getEmail());
        } else {
            likeDao.addEndLikedToCookie(response);
            likeDao.removeFromRatedDb(currentUserEmail.orElse(""));
            data.put("redirect", "/users?error=true&message=No more users to like.");
            data.put("avatarUrl", "");
            data.put("userName", "");
            data.put("userSurname", "");
            data.put("userEmail", "");
        }
        return data;
    }

    private Optional<User> getNextUser(String currentUser, HttpServletRequest request) {
        List<User> users = getShuffledUsers();
        if (users.isEmpty()) return Optional.empty();

        List<String> likedUsers = !currentUser.isEmpty() ? likeDao.getLikedUsersFromDb(currentUser) : new ArrayList<>();
        List<String> rated = !currentUser.isEmpty() ? likeDao.getRatedUsersFromDb(currentUser) : new ArrayList<>();

        List<User> availableUsers = users.stream()
                .filter(user -> !user.getEmail().equals(currentUser))
                .filter(user -> !rated.contains(user.getEmail()))
                .filter(user -> !likedUsers.contains(user.getEmail()))
                .toList();

        if (availableUsers.isEmpty()) return Optional.empty();

        User nextUser = availableUsers.getFirst();
        request.setAttribute("currentUserEmail", nextUser.getEmail());
        return Optional.of(nextUser);
    }

    public List<Map<String, Object>> getLickedUsersData(HttpServletRequest request) {
        String currentUserEmail = userController.getUserEmailFromCookie(request).orElse("");
        List<String> likedUsers = likeDao.getLikedUsersFromDb(currentUserEmail);
        List<User> users = userController.getUsers();
        List<Map<String, Object>> likedUsersData = new ArrayList<>();

        likedUsers.forEach(email -> {
            Optional<User> user = users.stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst();
            if (user.isPresent()) {
                Map<String, Object> likedUser = new HashMap<>();
                likedUser.put("id", user.get().getId());
                likedUser.put("avatarUrl", user.get().getAvatarUrl());
                likedUser.put("userName", user.get().getName());
                likedUser.put("userSurname", user.get().getSurname());
                likedUser.put("profession", user.get().getProfession());
                likedUser.put("lastVisitToString", user.get().getLastVisitToString());
                likedUser.put("daysSinceLastVisit", user.get().daysSinceLastVisit());
                likedUser.put("email", user.get().getEmail());
                likedUsersData.add(likedUser);
            }
        });
        return likedUsersData;
    }

    private List<User> getShuffledUsers() {
        List<User> users = userController.getUsers();
        Collections.shuffle(users);
        return users;
    }

    public void removeEndLikedFromCookie (HttpServletResponse response) {
        likeDao.removeEndLikedFromCookie(response);
    }

    public void startApp() {
        likeDao.createLikedTable();
        likeDao.createRatedTable();
    }
}
