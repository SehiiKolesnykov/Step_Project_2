package StepApp.service;

import StepApp.dao.UserDao;
import StepApp.dao.UserDaoImpl;
import StepApp.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class UserService {

    UserDao userDao;
    private final List<String> rated;

    public UserService() {
        this.userDao = new UserDaoImpl();
        rated = new ArrayList<>();
    }

    public boolean checkPassword(String email, String password) {
        HashMap<String, String> passwords = userDao.getUserPassword(email);
        return passwords.containsKey(email) && passwords.get(email).equals(password);
    }

    public boolean registerUser(User user) {
        return userDao.addUser(user);
    }

    public void setUserEmailToCookie (String userEmail, HttpServletResponse resp) {
        userDao.setUserEmailToCookie(userEmail, resp);
    }

    public Optional<String> getUserEmailFromCookie(HttpServletRequest request) {
       return userDao.getUserEmailFromCookie(request);
    }

    public Map<String, Object> likePageData (HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<>();
        Optional<String> currentUserEmail = getUserEmailFromCookie(request);

        Optional<String> endCookie = userDao.getLikedEnd(request);
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
                userDao.addToLikedDb(email, ratedUserEmail);
                addToRated(ratedUserEmail);
            });
        } else if ("dislike".equals(action) && ratedUserEmail != null) {
            addToRated(ratedUserEmail);
        }

        Optional<User> nextUser = getNextUser(currentUserEmail.orElse(""), request);
        if(nextUser.isPresent()){
            data.put("avatarUrl", nextUser.get().getAvatarUrl());
            data.put("userName", nextUser.get().getName());
            data.put("userSurname", nextUser.get().getSurname());
            data.put("userEmail", nextUser.get().getEmail());
        } else {
            userDao.addEndLikedToCookie(response);
            rated.clear();
            data.put("redirect", "/users?error=true&message=No more users to like.");
            data.put("avatarUrl", "");
            data.put("userName", "");
            data.put("userSurname", "");
            data.put("userEmail", "");
        }
        return data;
    }

    private void addToRated(String userEmail) {
        if (userEmail != null && !rated.contains(userEmail)) {
            rated.add(userEmail);
        }
    }

    private Optional<User> getNextUser(String currentUser, HttpServletRequest request) {
        List<User> users = getShuffledUsers();
        if (users.isEmpty()) return Optional.empty();

        List<String> likedUsers = !currentUser.isEmpty() ? userDao.getLikedUsersFromDb(currentUser) : new ArrayList<>();

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
        String currentUserEmail = getUserEmailFromCookie(request).orElse("");
        List<String> likedUsers = userDao.getLikedUsersFromDb(currentUserEmail);
        List<User> users = userDao.getUsers();
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

    public Map<String, Object> getUserDataByEmail(String email) {
        Map<String, Object> userData = new HashMap<>();
        Optional<User> user = userDao.getUserByEmail(email);
        if (user.isPresent()) {
            userData.put("id", user.get().getId());
            userData.put("avatarUrl", user.get().getAvatarUrl());
            userData.put("userName", user.get().getName());
            userData.put("userSurname", user.get().getSurname());
            userData.put("profession", user.get().getProfession());
            userData.put("lastVisit", user.get().getLastVisitToString());
        }
        return userData;
    }

    public boolean startApp() {
        if (usersCount() >= 10) return false;
        userDao.createUserTable();
        userDao.createLikedTable();

        long time = System.currentTimeMillis();
        Random random = new Random();

        createTestUser();

        newUsers(
                "male",
                userDao.getTestData("maleAvatars"),
                userDao.getTestData("maleNames"),
                userDao.getTestData("surnames"),
                userDao.getTestData("professions"),
                time,
                random,
                5);

        newUsers(
                "female",
                userDao.getTestData("femaleAvatars"),
                userDao.getTestData("femaleNames"),
                userDao.getTestData("surnames"),
                userDao.getTestData("professions"),
                time,
                random,
                5);
        return true;
    }

    private int usersCount() {
        return userDao.usersCount();
    }

    private String randomEmail(String name, String surname, Random random) {
        StringBuilder email = new StringBuilder()
                .append(name.toLowerCase().charAt(0))
                .append(surname.toLowerCase())
                .append(random.nextInt(100, 999))
                .append("@example.com");
        return email.toString();
    }

    private String generateRandomPassword(Random random) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            password.append(random.nextInt(9));
        }
        return password.toString();
    }

    private void newUsers(String gender, String[] avatars, String[] names, String[] surnames, String[] professions,
            long time, Random random, int amount) {

        for (int i = 0; i < amount; i++) {
            String name = names[random.nextInt(names.length)];
            String surname = surnames[random.nextInt(surnames.length)];
            String email = randomEmail(name, surname, random);
            String password = generateRandomPassword(random);
            String profession = professions[random.nextInt(professions.length)];
            String avatar;
            if (i < avatars.length) {
                avatar = avatars[i];
            } else {
                avatar = avatars[random.nextInt(avatars.length)];
            }
            User user = new User(name, surname, gender, avatar, profession, time, email, password);
            boolean success = registerUser(user);
            if (success) {
                System.out.println("User " + i + " " + name + surname + " registered");
            }
        }
    }

    private void createTestUser() {
        long time = System.currentTimeMillis();

        User testUser = new User(
                "Name",
                "Surname",
                "male",
                "https://media.istockphoto.com/id/1450340623/uk/%D1%84%D0%BE%D1%82%D0%BE/%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82-%D1%83%D1%81%D0%BF%D1%96%D1%88%D0%BD%D0%BE%D0%B3%D0%BE-%D0%B7%D1%80%D1%96%D0%BB%D0%BE%D0%B3%D0%BE-%D0%B1%D0%BE%D1%81%D0%B0-%D1%81%D1%82%D0%B0%D1%80%D1%88%D0%BE%D0%B3%D0%BE-%D0%B1%D1%96%D0%B7%D0%BD%D0%B5%D1%81%D0%BC%D0%B5%D0%BD%D0%B0-%D0%B2-%D0%BE%D0%BA%D1%83%D0%BB%D1%8F%D1%80%D0%B0%D1%85-%D0%B0%D0%B7%D1%96%D0%B0%D1%82%D0%B0-%D1%89%D0%BE-%D0%B4%D0%B8%D0%B2%D0%B8%D1%82%D1%8C%D1%81%D1%8F-%D0%B2-%D0%BA%D0%B0%D0%BC%D0%B5%D1%80%D1%83-%D1%96.jpg?s=612x612&w=0&k=20&c=Lcz61N-HxBFuYRhQU1lGo7e8e54gdsTeEYsh1oFrJ2k=",
                "Tester",
                time,
                "test@example.com",
                "123456"
        );

        boolean isCreateTestUser = registerUser(testUser);
        if (isCreateTestUser) {
            System.out.println("Test user created");
        }
    }

    private List<User> getShuffledUsers() {
        List<User> users = userDao.getUsers();
        Collections.shuffle(users);
        return users;
    }

    public Optional<String> getUserEmailById(int id) {
        return userDao.getUserEmailById(id);
    }

    public void removeEndLikedFromCookie (HttpServletResponse response) {
        userDao.removeEndLikedFromCookie(response);
    }
}
