package com.commenau.service;

import com.commenau.dao.ConversationDAO;
import com.commenau.dao.UserDAO;
import com.commenau.dto.MessageDTO;
import com.commenau.dto.UserChatDTO;
import com.commenau.mapper.MessageMapper;
import com.commenau.model.Conversation;
import com.commenau.model.Message;
import com.commenau.model.User;
import com.commenau.pagination.PageRequest;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ConversationService {


    private ConversationDAO conversationDAO = new ConversationDAO();

    private UserDAO userDAO = new UserDAO();

    private MessageMapper messageMapper = new MessageMapper();

    public int getConversationById(int participantId) {
        int id = conversationDAO.findConservationById(participantId);

        //id isn't exists
        if(id == 0) {
            id = conversationDAO.createConversation(participantId);
        }
        return id;
    }

    public Message saveMessage(Message message) {
        return conversationDAO.saveMessage(message);
    }

    public List<UserChatDTO> getUsersChatByRelativeName(String query) {
        List<Conversation> conversations = conversationDAO.getAllConversations();
        List<UserChatDTO> userChatDTOS = new ArrayList<>();
        for (var x : conversations) {
            UserChatDTO userChatDTO = UserChatDTO.builder().build();
            User user = userDAO.getFirstNameAndLastName((long) x.getParticipantId());
            userChatDTO.setId(x.getParticipantId());
            userChatDTO.setName(user.getLastName() + " " + user.getFirstName());
            if (userChatDTO.getName().toLowerCase().contains(query.toLowerCase())) {
                userChatDTOS.add(userChatDTO);
            }
        }
        return userChatDTOS;
    }

    public List<UserChatDTO> getUsersChat() {
        List<Conversation> conversations = conversationDAO.getAllConversations();
        List<UserChatDTO> userChatDTOS = new ArrayList<>();

        for (var x : conversations) {
            UserChatDTO userChatDTO = UserChatDTO.builder().build();
            Message lastMessage = conversationDAO.getLastMessage(x.getParticipantId());
            if (lastMessage == null) continue;
            User user = userDAO.getFirstNameAndLastName((long) x.getParticipantId());

            MessageDTO messageDTO = MessageDTO.builder().build();
            messageDTO.setSenderId(lastMessage.getSenderId());
            messageDTO.setTime(lastMessage.getSendTime());
            messageDTO.setContent(lastMessage.getContent());
            messageDTO.setViewed(lastMessage.isViewed());


            userChatDTO.setId(x.getParticipantId());
            userChatDTO.setName(user.getLastName() + " " + user.getFirstName());
            userChatDTO.setMessage(messageDTO);
            userChatDTOS.add(userChatDTO);
        }
        return userChatDTOS.stream().sorted(Comparator.comparing(n -> n.getMessage().getTime())).toList();
    }

    public List<MessageDTO> getMessages(int participantId, PageRequest pageRequest) {
        List<Message> messages = conversationDAO.getMessages(participantId, pageRequest);
        return messageMapper.toDTO(messages, MessageDTO.class);
    }

    public boolean updateViewed(int particapantId, int ownerId) {
        return conversationDAO.updateViewed(particapantId, ownerId);
    }
}
