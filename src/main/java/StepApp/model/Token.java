package StepApp.model;

import java.util.UUID;

public class Token {

    String name;
    UUID token;

    public Token(UUID token) {
        this.name = "USER_TOKEN";
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public UUID getToken() {
        return token;
    }
}
