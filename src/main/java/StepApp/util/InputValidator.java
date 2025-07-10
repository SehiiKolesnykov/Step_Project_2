package StepApp.util;

import java.util.regex.Pattern;

public class InputValidator {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_PATTERN = "^(?=\\S+$)[a-zA-Z0-9]{5,20}$";
    private static final Pattern EMAIL_PATTERN_OBJ = Pattern.compile(EMAIL_PATTERN);

    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final int MAX_EMAIL_LENGTH = 50;

    public boolean checkInput(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) return false;
        if (email.length() > MAX_EMAIL_LENGTH || password.length() > MAX_PASSWORD_LENGTH ||
                password.length() < MIN_PASSWORD_LENGTH) return false;
        if (!isValidEmail(email)) return false;
        if (!isValidPassword(password)) return false;
        return true;
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN_OBJ.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }

}
