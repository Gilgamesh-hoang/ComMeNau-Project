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
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConversationService {
    private ConversationDAO conversationDAO = new ConversationDAO();

    private UserDAO userDAO = new UserDAO();

    private MessageMapper messageMapper = new MessageMapper();

    public int getConversationById(long participantId) {
        int id = conversationDAO.findConservationById(participantId);

        //id isn't exists
        if (id == 0) {
            id = conversationDAO.createConversation(participantId);
        }
        return id;
    }

    public Message saveMessage(Message message) {
        return conversationDAO.saveMessage(message);
    }

    public List<UserChatDTO> getUsersByName(String name) {
        if (StringUtils.isBlank(name)) {
            return getAllUserChats();
        } else {
            return mapToUserChatDTO(conversationDAO.findConversationsByName(name));
        }
    }

    public List<UserChatDTO> getAllUserChats() {
        return mapToUserChatDTO(conversationDAO.findAllConversations());
    }

    private List<UserChatDTO> mapToUserChatDTO(List<Conversation> conversations) {
        return conversations.stream().map(conversation -> {
            Message msg = conversationDAO.getLastMessage(conversation.getParticipantId());
            MessageDTO lastMessage = messageMapper.toDTO(msg);
            User user = userDAO.getFullName((long) conversation.getParticipantId());
            return UserChatDTO.builder().userId(conversation.getParticipantId()).message(lastMessage)
                    .fullName(StringUtils.joinWith(" ", user.getLastName(), user.getFirstName())).build();
        }).sorted(Comparator.comparing(chat -> chat.getMessage().getTime())).toList();
    }

    public List<MessageDTO> getMessages(int participantId, PageRequest pageRequest) {
        List<Message> messages = conversationDAO.getMessages(participantId, pageRequest);
        return messages.stream().map(message -> messageMapper.toDTO(message)).collect(Collectors.toList());
    }

    public boolean updateViewed(int participantId, int ownerId) {
        return conversationDAO.updateViewed(participantId, ownerId);
    }
}
