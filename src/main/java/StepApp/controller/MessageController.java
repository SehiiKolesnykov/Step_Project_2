package StepApp.controller;

import StepApp.service.MessageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class MessageController {

    private final MessageService messageService;

    public MessageController() {
        this.messageService = new MessageService();
    }

    public Map<String, Object> getChatData(HttpServletRequest request, HttpServletResponse response, int recipientId) {
        return messageService.getChatData(request, recipientId);
    }

    public boolean sendMessage(HttpServletRequest request, int recipientId, String message) {
        return messageService.sendMessage(request, recipientId, message);
    }

    public void startApp() { messageService.startApp(); }

    public int getRecipientId(HttpServletRequest request) throws IOException {
        return messageService.getIdFromPath(request);
    }
}
