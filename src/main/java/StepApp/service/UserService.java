package StepApp.service;

import StepApp.dao.UserDao;
import StepApp.dao.UserDaoImpl;
import StepApp.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Pattern;

public class UserService {

    UserDao userDao;

    public UserService() {
        this.userDao = new UserDaoImpl();
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

    public Optional<String> getUserEmailById(int id) {
        return userDao.getUserEmailById(id);
    }

    public void setLastVisitToNow(HttpServletRequest request) {
        userDao.updateUserLastLogin(request);
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public Optional<User> checkNewUserData(String name, String surname, String gender, String email, String password) {
        String nameSurnamePattern = "^[a-zA-Z]{2,50}$";
        String genderPattern = "^(male|female)$";
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String passwordPattern = "^(?=\\S+$)[a-zA-Z0-9]{5,20}$";

        if ( name == null || name.isBlank() ||
                surname == null || surname.isBlank() ||
                gender == null || gender.isBlank() ||
                email == null || email.isBlank() ||
                password == null || password.isBlank()) {

            return Optional.empty();
        }

        if (!Pattern.compile(nameSurnamePattern).matcher(surname).matches()) {
            return Optional.empty();
        }

        if (!Pattern.compile(genderPattern).matcher(gender).matches()) {
            return Optional.empty();
        }

        if (!Pattern.compile(emailPattern).matcher(email).matches()) {
            return Optional.empty();
        }

        if (!Pattern.compile(passwordPattern).matcher(password).matches()) {
            return Optional.empty();
        }

        String avatarUrl = userDao.getDefaultAvatar(gender);
        String profession = userDao.getDefaultProfession();

        return Optional.of(new User(name, surname, gender, avatarUrl, profession, System.currentTimeMillis(), email, password));
    }

    public boolean startApp() {
        if (usersCount() >= 10) return false;
        userDao.createUserTable();

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

        registerUser(testUser);
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
            registerUser(user);
        }
    }

}


