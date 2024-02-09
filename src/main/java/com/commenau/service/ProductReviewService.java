package com.commenau.service;

import com.commenau.dao.ProductReviewDAO;
import com.commenau.dao.UserDAO;
import com.commenau.dto.ProductReviewDTO;
import com.commenau.mapper.ProductReviewMapper;
import com.commenau.model.ProductReview;
import com.commenau.model.User;
import com.commenau.pagination.PageRequest;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ProductReviewService{
    @Inject
    private ProductReviewDAO reviewDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private ProductReviewMapper reviewMapper;

    public boolean save(ProductReview review) {
        return reviewDAO.save(review);
    }

    public List<ProductReviewDTO> getReviewByProductId(int productId, PageRequest pageRequest) {
        return reviewDAO.getReviewByProductId(productId, pageRequest).stream().map(review -> {
            ProductReviewDTO dto = reviewMapper.toDTO(review, ProductReviewDTO.class);
            User user = userDAO.getFullName(review.getUserId());
            dto.setLastName(user.getLastName());
            dto.setFirstName(user.getFirstName());
            return dto;
        }).collect(Collectors.toList());
    }

}
