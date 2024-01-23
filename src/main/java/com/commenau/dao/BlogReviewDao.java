package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.dto.BlogReviewDTO;
import com.commenau.model.BlogReview;

import java.util.List;

public class BlogReviewDao {

    public List<BlogReviewDTO> findReviewByBlogId(int blogId) {
        String sql = "SELECT content, br.createdAt, concat(lastName, ' ', firstName) AS fullName " +
                "FROM blog_reviews br INNER JOIN users ON users.id = br.userId " +
                "WHERE blogId = ? ORDER BY br.createdAt DESC LIMIT 5";
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind(0, blogId).map((rs, ctx) -> {
                    return BlogReviewDTO.builder().fullName(rs.getString("fullName")).content(rs.getString("content"))
                            .createdAt(rs.getTimestamp("createdAt")).build();
                }).list()
        );
    }

    public int countAllByBlogId(int blogId) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM blog_reviews WHERE blogId = ? ")
                        .bind(0, blogId).mapTo(Integer.class).stream().findFirst().orElse(0)
        );
    }

    public void saveReview(BlogReview blogReview) {
        JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("INSERT INTO blog_reviews (blogId, userId, content) VALUES (:blogId, :userId, :content)")
                        .bindBean(blogReview).execute()
        );
    }

    public boolean deleteByBlogId(Integer blogId) throws Exception {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM blog_reviews WHERE blogId = :blogId")
                        .bind("blogId", blogId)
                        .execute());
        return result >= 0;
    }


}
