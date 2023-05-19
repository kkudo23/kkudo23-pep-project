package Controller;

import DAO.AccountDAO;
import DAO.AccountDAOImpl;
import DAO.MessageDAO;
import DAO.MessageDAOImpl;
import Model.Account;
import Model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class SocialMediaController {
    private final AccountDAO accountDAO;
    private final MessageDAO messageDAO;
    private final ObjectMapper objectMapper;

    public SocialMediaController() {
        this.accountDAO = new AccountDAOImpl();
        this.messageDAO = new MessageDAOImpl();
        this.objectMapper = new ObjectMapper();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/:message_id", this::getMessageByIdHandler);
        app.get("/accounts/:account_id/messages", this::getMessagesByUserHandler);
        app.post("/register", this::registerUserHandler);
        app.post("/login", this::loginUserHandler);
        app.post("/messages", this::createMessageHandler);
        app.patch("/messages/:message_id", this::updateMessageHandler);
        app.delete("/messages/:message_id", this::deleteMessageHandler);

        return app;
    }

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageDAO.getAllMessages();
        context.json(messages);
    }

    private void getMessageByIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageDAO.getMessageById(messageId);
        if (message != null) {
            context.json(message);
        } else {
            context.status(404).result("Message not found");
        }
    }

    private void getMessagesByUserHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageDAO.getMessagesByUser(accountId);
        context.json(messages);
    }

    private void registerUserHandler(Context context) {
        try {
            Account account = objectMapper.readValue(context.body(), Account.class);
            Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
            if (existingAccount == null) {
                Account createdAccount = accountDAO.createAccount(account);
                context.json(createdAccount);
            } else {
                context.status(400).result("Username already exists");
            }
        } catch (Exception e) {
            context.status(400).result("Invalid request body");
        }
    }

    private void loginUserHandler(Context context) {
        try {
            Account account = objectMapper.readValue(context.body(), Account.class);
            Account existingAccount = accountDAO.getAccountByUsernameAndPassword(
                    account.getUsername(), account.getPassword());
            if (existingAccount != null) {
                context.json(existingAccount);
            } else {
                context.status(401).result("Invalid credentials");
            }
        } catch (Exception e) {
            context.status(400).result("Invalid request body");
        }
    }

    private void createMessageHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        Account account = accountDAO.getAccountById(accountId);
        if (account != null) {
            try {
                Message message = context.bodyAsClass(Message.class);
                message.setPosted_by(accountId);
                Message createdMessage = messageDAO.createMessage(message);
                context.json(createdMessage);
            } catch (Exception e) {
                e.printStackTrace();
                context.status(400).result("Invalid message data");
            }
        } else {
            context.status(404).result("Account not found");
        }
    }
    

    private void updateMessageHandler(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message existingMessage = messageDAO.getMessageById(messageId);
            if (existingMessage != null) {
                Message updatedMessage = objectMapper.readValue(context.body(), Message.class);
                updatedMessage.setMessage_id(messageId);
                boolean success = messageDAO.updateMessage(updatedMessage);
                if (success) {
                    context.result("Message updated successfully");
                } else {
                    context.status(500).result("Failed to update message");
                }
            } else {
                context.status(404).result("Message not found");
            }
        } catch (Exception e) {
            context.status(400).result("Invalid request body");
        }
    }

    private void deleteMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        boolean success = messageDAO.deleteMessage(messageId);
        if (success) {
            context.result("Message deleted successfully");
        } else {
            context.status(404).result("Message not found");
        }
    }
}
