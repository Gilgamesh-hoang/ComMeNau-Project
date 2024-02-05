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

@ServerEndpoint(value  ="/chat/{userID}")
public class WebSocketService {

    private ConversationService conversationService = new ConversationService();
    private UserDAO userDAO = new UserDAO();

    @OnOpen
    public void onOpen(@PathParam("userID") final int userID, Session session) {
        if (!userDAO.isAdmin(userID)) {
            session.getUserProperties().put("role", "user");
            session.getUserProperties().put("userID", userID);
        } else {
            session.getUserProperties().put("role", "admin");
            session.getUserProperties().put("userID", userID);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        Gson gson = new Gson();
        User admin = userDAO.findAdmin();
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        String role = session.getUserProperties().get("role").toString();
        if (role.equalsIgnoreCase("user")) {
            int senderId = (int) session.getUserProperties().get("userID");
            int conversationId = conversationService.getConversationById(senderId);
            Message msg = Message.builder().senderId(senderId).recipientId(admin.getId())
                    .conversationId(conversationId).content(chatMessage.getMsg()).viewed(false).build();
            msg = conversationService.saveMessage(msg);
            if (msg != null) {
                for (Session s : session.getOpenSessions()) {
                    if (s.getUserProperties().get("role").toString().equalsIgnoreCase("admin")) {
                        s.getBasicRemote().sendText(gson.toJson(msg));
                    }
                }
            }
        } else {
            int conversationId = conversationService.getConversationById(chatMessage.getRecipientId());
            for (Session s : session.getOpenSessions()) {
                if (s.getUserProperties().get("userID").toString().equalsIgnoreCase(chatMessage.getRecipientId() + "")) {
                    Message msg = Message.builder().senderId(admin.getId()).recipientId(chatMessage.getRecipientId())
                            .conversationId(conversationId).content(chatMessage.getMsg()).viewed(false).build();
                    msg = conversationService.saveMessage(msg);
                    if (msg != null) {
                        s.getBasicRemote().sendText(gson.toJson(msg));
                    }
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        // Được gọi khi kết nối WebSocket bị đóng
    }
}
