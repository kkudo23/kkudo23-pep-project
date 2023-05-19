package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

import java.util.List;

public class SocialMediaService {
    private final AccountDAO accountDAO;
    private final MessageDAO messageDAO;

    public SocialMediaService(AccountDAO accountDAO, MessageDAO messageDAO) {
        this.accountDAO = accountDAO;
        this.messageDAO = messageDAO;
    }

    public Account registerUser(Account account) {
        return accountDAO.createAccount(account);
    }

    public Account loginUser(Account account) {
        return accountDAO.getAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
    }

    public Message createMessage(Message message) {
        return messageDAO.createMessage(message);
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.getMessagesByUser(accountId);
    }

    public boolean updateMessage(Message message) {
        return messageDAO.updateMessage(message);
    }

    public boolean deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }
}
