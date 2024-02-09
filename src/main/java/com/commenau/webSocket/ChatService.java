package com.commenau.webSocket;

import com.commenau.dao.UserDAO;
import com.commenau.model.ChatMessage;
import com.commenau.model.Message;
import com.commenau.model.User;
import com.commenau.service.ConversationService;
import com.google.gson.Gson;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/chat/{userID}")
public class ChatService {

    private ConversationService conversationService = new ConversationService();
    private UserDAO userDAO = new UserDAO();

    // Method called when a new WebSocket connection is opened
    @OnOpen
    public void onOpen(@PathParam("userID") final int userID, Session session) {
        // Check if the user is an admin or a regular user
        if (!userDAO.isAdmin(userID)) {
            // Set user role and ID in the session properties for non-admin users
            session.getUserProperties().put("role", "user");
            session.getUserProperties().put("userID", userID);
        } else {
            // Set admin role and ID in the session properties for admin users
            session.getUserProperties().put("role", "admin");
            session.getUserProperties().put("userID", userID);
        }

    }

    // Method called when a message is received from a WebSocket client
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        // Create a Gson instance for JSON serialization/deserialization
        Gson gson = new Gson();
        // Find the admin user
        User admin = userDAO.findAdmin();
        // Deserialize the received message into a ChatMessage object
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        // Get the user role from the session properties
        String role = session.getUserProperties().get("role").toString();
        // Process the message based on user role
        if (role.equalsIgnoreCase("user")) {
            int senderId = (int) session.getUserProperties().get("userID");
            int conversationId = conversationService.getConversationById(senderId);

            // Create a Message object and save it to the conversationService
            Message msg = Message.builder().senderId(senderId).recipientId(admin.getId())
                    .conversationId(conversationId).content(chatMessage.getMsg()).viewed(false).build();
            msg = conversationService.saveMessage(msg);

            // Broadcast the message to all open admin sessions
            if (msg != null) {
                for (Session s : session.getOpenSessions()) {
                    if (s.getUserProperties().get("role").toString().equalsIgnoreCase("admin")) {
                        s.getBasicRemote().sendText(gson.toJson(msg));
                    }
                }
            }
        } else {
            // For admin, get the conversationId and create/save the message
            int conversationId = conversationService.getConversationById(chatMessage.getRecipientId());
            for (Session s : session.getOpenSessions()) {
                if (s.getUserProperties().get("userID").toString().equalsIgnoreCase(chatMessage.getRecipientId() + "")) {
                    Message msg = Message.builder().senderId(admin.getId()).recipientId(chatMessage.getRecipientId())
                            .conversationId(conversationId).content(chatMessage.getMsg()).viewed(false).build();
                    msg = conversationService.saveMessage(msg);

                    // Send the message to the specific user session
                    if (msg != null) {
                        s.getBasicRemote().sendText(gson.toJson(msg));
                    }
                }
            }
        }
    }

    // Method called when a WebSocket connection is closed
    @OnClose
    public void onClose(Session session) {
        // This method is called when the WebSocket connection is closed
    }
}