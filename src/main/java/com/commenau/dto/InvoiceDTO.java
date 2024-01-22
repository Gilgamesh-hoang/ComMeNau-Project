package com.commenau.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
    private Integer id;
    private String userFullName;
    private String userEmail;
    private String checkoutMethod;
    private String status;
    private Double shippingFee;
    private Double total;
    private String address;
    private String phoneNumber;
    private String paymentMethod;
    private Timestamp updatedAt;
    private Timestamp createdAt;
    //image,name, list?
}
