package StepApp.servlet;

import StepApp.controller.UserController;
import StepApp.util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class LikePageServlet extends HttpServlet {

    private final TemplateEngine templateEngine;
    private final UserController userController;

    public LikePageServlet(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.userController = new UserController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Object> data = userController.likePageData(request, response);

        System.out.println(data);

        if (data.containsKey("redirect")) {
            response.sendRedirect(request.getContextPath() + "/liked");
            return;
        }
        templateEngine.render("like-page.ftl", data, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        doGet(req, resp);
    }
}
