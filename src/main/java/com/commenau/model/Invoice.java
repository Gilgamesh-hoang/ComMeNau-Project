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
public class Invoice {
	private Integer id;
	private Long userId;
	private Double shippingFee;
	private String note;
	private String fullName;
	private String address;
	private String phoneNumber;
	private String email;
	private String paymentMethod;
	private Timestamp createdAt;
}