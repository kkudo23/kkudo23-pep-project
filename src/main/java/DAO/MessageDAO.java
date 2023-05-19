package DAO;

import Model.Message;

import java.util.List;

public interface MessageDAO {
    Message createMessage(Message message);
    Message getMessageById(int messageId);
    List<Message> getAllMessages();
    List<Message> getMessagesByUser(int accountId);
    boolean updateMessage(Message message);
    boolean deleteMessage(int messageId);
}

