package StepApp.service;

import StepApp.dao.TokenDao;
import StepApp.dao.TokenDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

public class TokenService {

    private final TokenDao tokenDao;

    public TokenService() {
        this.tokenDao = new TokenDaoImpl();
    }

    public void authorisation(String email, HttpServletResponse resp) {
        UUID token = UUID.randomUUID();
        tokenDao.setTokenToDB(token, email);
        tokenDao.setTokenToCookie(token,resp);
    }

    public void removeToken(UUID token, HttpServletResponse resp) {
        tokenDao.removeTokenFromDB(token);
        tokenDao.removeTokenFromCookie(resp);
    }

    public Optional<String> checkToken(UUID token) {
        return tokenDao.checkToken(token);
    }

    public Optional<UUID> getTokenFromCookie(HttpServletRequest request) {
        return tokenDao.getTokenFromCookie(request);
    }

    public Optional<UUID> getTokenFromDB(String email) {
        return tokenDao.getTokenFromDB(email);
    }

    public void startAppToken() {
        tokenDao.createUserTable();
    }

}
