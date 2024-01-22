package com.commenau.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private int id;
    private int categoryId;
    private String avatar;
    private String categoryName;
    private String name;
    private String description;
    private int available;
    private double price;
    private float discount;
    private float rate;
    private int amountOfReview;
    private boolean status;
    private boolean wishlist;
    private List<String> images;

}