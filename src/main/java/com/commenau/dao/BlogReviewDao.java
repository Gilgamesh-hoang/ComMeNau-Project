package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.model.BlogReview;

import java.util.List;
public class BlogReviewDao {

    public List<BlogReview> getBlogReviewToTime(int blogId) {
        List<BlogReview> blogReviews = JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery("SELECT br.userId , br.content, br.createdAt " +
                            "from blog_reviews br where br.blogId = ? order by br.createdAt desc LIMIT 5")
                    .bind(0, blogId).mapToBean(BlogReview.class)
                    .list();
        });
        return blogReviews;
    }
    public List<BlogReview> getAllBlogReview(int blogId) {
        List<BlogReview> blogReviews = JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery("SELECT br.userId from blog_reviews br where br.blogId = ? ")
                    .bind(0, blogId).mapToBean(BlogReview.class)
                    .list();
        });
        return blogReviews;
    }

    public void saveReviewByUser(BlogReview blogReview) {
        JDBIConnector.getInstance().inTransaction((handle -> {
            return handle.createUpdate("INSERT INTO blog_reviews (blogId, userId, content) VALUES (?,?,?)")
                    .bind(0, blogReview.getBlogId())
                    .bind(1, blogReview.getUserId())
                    .bind(2, blogReview.getContent())
                    .execute();
        }));
    }

    public boolean deleteByBlogId(Integer blogId) throws Exception {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM blog_reviews WHERE blogId = :blogId")
                        .bind("blogId", blogId)
                        .execute());
        return result >= 0;
    }
}
