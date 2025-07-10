package StepApp.servlet;

import StepApp.controller.LikeController;
import StepApp.controller.UserController;
import StepApp.util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikedPageServlet extends HttpServlet {

    private final TemplateEngine templateEngine;
    private final LikeController likeController;

    public LikedPageServlet(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.likeController = new LikeController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Object> data = new HashMap<>();

        List<Map<String, Object>> likedUsers = likeController.getLikedUsersData(request);
        data.put("likedUsers", likedUsers);

        templateEngine.render("people-list.ftl", data, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
