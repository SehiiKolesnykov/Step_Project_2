package StepApp.servlet;

import StepApp.controller.MessageController;
import StepApp.util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ChatServlet extends HttpServlet {

    private final TemplateEngine templateEngine;
    private final MessageController messageController;

    public ChatServlet(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.messageController = new MessageController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int recipientId = messageController.getRecipientId(request);
        System.out.println(recipientId);
        Map<String, Object> data = messageController.getChatData(request, response, recipientId);
        System.out.println(data);

        templateEngine.render("chat.ftl", data, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int recipientId = messageController.getRecipientId(req);
        String message = req.getParameter("message");
        messageController.sendMessage(req, recipientId, message);
        doGet(req, resp);
    }


}
