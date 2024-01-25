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
    private String fullName;
    private String email;
    private String status;
    private double shippingFee;
    private Integer total;
    private String address;
    private String phoneNumber;
    private String paymentMethod;
    private Timestamp createdAt;

}
