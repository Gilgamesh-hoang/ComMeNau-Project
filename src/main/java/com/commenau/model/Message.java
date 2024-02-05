package com.commenau.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private int id;
    private int conversationId;
    private long senderId;
    private long recipientId;
//    private int senderId;
//    private int recipientId;
    private boolean viewed;
    private String content;
    private Timestamp sendTime;

}
