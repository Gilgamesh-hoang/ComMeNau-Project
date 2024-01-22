package com.commenau.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private int id;
    private String fullName;
    private String email;
    private String message;
    private long userId;
    private Timestamp createdAt;

}