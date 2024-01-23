package com.commenau.service;

import com.commenau.dao.BlogReviewDao;
import com.commenau.dto.BlogReviewDTO;
import com.commenau.model.BlogReview;

import javax.inject.Inject;
import java.util.List;

public class BlogReviewService {
    @Inject
    private BlogReviewDao blogReviewDao;

    public List<BlogReviewDTO> getReviewByBlogId(int blogId){
        return blogReviewDao.findReviewByBlogId(blogId);
    }

    public int numberReviews(int blogId) {
        return blogReviewDao.countAllByBlogId(blogId);
    }

    public void saveReview(BlogReview blogReview) {
        blogReviewDao.saveReview(blogReview);
    }

}
