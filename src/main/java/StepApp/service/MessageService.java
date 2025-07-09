package StepApp.service;

import StepApp.controller.UserController;
import StepApp.dao.MessageDao;
import StepApp.dao.MessageDaoImpl;
import StepApp.model.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class MessageService {

    private final MessageDao messageDao;
    private final UserController userController;

    public MessageService() {
        this.messageDao = new MessageDaoImpl();
        this.userController = new UserController();
    }

    public void startApp() {
        messageDao.createMessageTable();
    }

    public boolean sendMessage(HttpServletRequest request, int recipientId, String message) {
        Optional<String> currentUserEmail = userController.getUserEmailFromCookie(request);
        Optional<String> recipientEmail = userController.getUserEmailById(recipientId);

        if (currentUserEmail.isEmpty() || recipientEmail.isEmpty() || message == null || message.trim().isEmpty())
            return false;
        long currentTime = System.currentTimeMillis();
        return messageDao.saveMessage(currentUserEmail.get(), recipientEmail.get(), message, currentTime);
    }

    public Map<String, Object> getChatData(HttpServletRequest request, int id) {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> messages = new ArrayList<>();

        Optional<String> currentUser = userController.getUserEmailFromCookie(request);
        Optional<String> recipientEmail = userController.getUserEmailById(id);


        if (currentUser.isEmpty() || recipientEmail.isEmpty()) {
            data.put("error", "Invalid user or recipient");
            return data;
        };

        List<Message> messageList = messageDao.getMessagesBetweenUsers(currentUser.get(), recipientEmail.get());

        Map<String, Object> recipientData = userController.getUserDataByEmail(recipientEmail.get());
        data.put("recipientName", recipientData.getOrDefault("userName", "")
                + " " + recipientData.getOrDefault("userSurname", ""));
        data.put("recipientAvatar", recipientData.getOrDefault("avatarUrl", ""));
        data.put("recipientEmail", recipientEmail);
        data.put("recipientId", recipientData.getOrDefault("id", ""));

        for (Message message : messageList) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("isSent", message.getSrc().equals(currentUser.get()));
            messageData.put("text", message.getMessage());
            messageData.put("time", message.getFormattedDate());
            messageData.put("avatar", message.getSrc().equals(currentUser.get()) ? "" : data.get("recipientAvatar"));
            messages.add(messageData);
        }
        data.put("messages", messages);
        return data;
    }

    public int getIdFromPath(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        if (path.length < 2) return -1;
        try {
            return Integer.parseInt(path[1]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
