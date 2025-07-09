package StepApp.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

public interface TokenDao{

    Optional<UUID> getTokenFromCookie (HttpServletRequest request);
    Optional<UUID> getTokenFromDB (String email);
    Optional<String> checkToken (UUID token);
    void setTokenToCookie (UUID token, HttpServletResponse resp);
    void setTokenToDB (UUID token, String email);
    void removeTokenFromCookie (HttpServletResponse resp);
    void removeTokenFromDB (UUID token);
    void createUserTable();
}
