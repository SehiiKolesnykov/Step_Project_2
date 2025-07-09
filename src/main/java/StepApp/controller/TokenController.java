package StepApp.controller;

import StepApp.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

public class TokenController {

    TokenService tokenService;

    public TokenController() {
        this.tokenService = new TokenService();
    }

    public void authorisation(String email, HttpServletResponse resp) {
        tokenService.authorisation(email, resp);
    }

    public void removeToken(UUID token, HttpServletResponse resp) {
        tokenService.removeToken(token, resp);
    }

    public Optional<UUID> getToken (HttpServletRequest request) {
        return tokenService.getTokenFromCookie(request);
    }

    public Optional<String> checkToken (UUID token) {
        return tokenService.checkToken(token);
    }

    public void startAppToken() {
        tokenService.startAppToken();
    }

}
