package com.commenau.mapper;

import com.commenau.dto.MessageDTO;
import com.commenau.model.Message;

public class MessageMapper extends GeneralMapper<MessageDTO, Message> {

    public MessageDTO toDTO(Message entity) {
        if (entity == null) {
            return null;
        }

        MessageDTO dto = new MessageDTO();
        if (entity.getContent() != null) {
            dto.setContent(entity.getContent());
        }
        if (entity.getSendTime() != null) {
            dto.setTime(entity.getSendTime());
        }
        dto.setSenderId(entity.getSenderId());
        dto.setViewed(entity.isViewed());

        return dto;
    }
}
