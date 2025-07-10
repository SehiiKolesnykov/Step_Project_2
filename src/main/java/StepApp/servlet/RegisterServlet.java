package StepApp.servlet;

import StepApp.controller.UserController;
import StepApp.model.User;
import StepApp.util.TemplateEngine;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegisterServlet extends HttpServlet {

    private final TemplateEngine templateEngine;
    private final UserController userController;

    public RegisterServlet(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.userController = new UserController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> data = new HashMap<>();

        templateEngine.render("register.ftl", data, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String gender = req.getParameter("gender");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        System.out.println(name + " " + surname + " " + gender + " " + email + " " + password);
        Optional<User> newUser = userController.checkNewUserData(name, surname, gender, email, password);

        if (newUser.isEmpty()) resp.sendRedirect(req.getContextPath() + "/register");

        else {
            boolean success = userController.registerUser(newUser.get());
            if (success) {resp.sendRedirect(req.getContextPath() + "/login");}
        }
    }
}
