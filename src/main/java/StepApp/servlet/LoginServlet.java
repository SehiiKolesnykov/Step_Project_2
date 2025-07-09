package StepApp.servlet;

import StepApp.controller.TokenController;
import StepApp.controller.UserController;
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

    public LoginServlet(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.userController = new UserController();
        this.tokenController = new TokenController();
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

        if (userController.checkPassword(email, password)) {
            tokenController.authorisation(email, resp);
            userController.setUserEmailToCookie(email, resp);
            userController.removeEndLikedFromCookie(resp);
            resp.sendRedirect(req.getContextPath() + "/users");
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }

    }

}
