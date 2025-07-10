package StepApp.servlet;

import StepApp.controller.LikeController;
import StepApp.controller.TokenController;
import StepApp.controller.UserController;
import StepApp.util.InputValidator;
import StepApp.util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {

    private final UserController userController;
    private final TokenController tokenController;
    private final TemplateEngine templateEngine;
    private final LikeController likeController;
    private final InputValidator inputValidator;

    public LoginServlet(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.userController = new UserController();
        this.tokenController = new TokenController();
        this.likeController = new LikeController();
        this.inputValidator = new InputValidator();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Object> data = new HashMap<>();

        templateEngine.render("login.ftl", data, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (inputValidator.checkInput(email, password)) {
            if (userController.checkPassword(email, password)) {
                tokenController.authorisation(email, resp);
                userController.setUserEmailToCookie(email, resp);
                likeController.removeEndLikedFromCookie(resp);
                userController.setLastVisitToNow(req);
                resp.sendRedirect(req.getContextPath() + "/users");
            } else {
                resp.sendRedirect(req.getContextPath() + "/login");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }

    }

}
