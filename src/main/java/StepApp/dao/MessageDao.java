package StepApp.dao;

import StepApp.model.Message;

import java.util.List;

public interface MessageDao {
    void createMessageTable();
    boolean saveMessage(String src, String dst, String message, Long date);
    List<Message> getMessagesBetweenUsers(String user1, String user2);
}
