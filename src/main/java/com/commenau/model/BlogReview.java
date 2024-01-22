package com.commenau.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogReview {
    private int id;
    private int blogId;
    private long userId;
    private String content;
    private Timestamp createdAt;

//    public String userCreated() {
//        return user.fullName();
//    }
//	public String getImage(long userId){
//		return userDAO.getUserById(userId).get();
//	}
}