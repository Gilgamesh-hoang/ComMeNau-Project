package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.model.ProductReview;
import com.commenau.pagination.PageRequest;
import com.commenau.util.PagingUtil;

import java.util.List;

public class ProductReviewDAO {
    public boolean save(ProductReview review) {
        String sql = "INSERT INTO product_reviews(productId,userId,rating,content) VALUES (:productId,:userId,:rating,:content)";
        int affectedRow = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate(sql).bind("productId", review.getProductId()).bind("userId", review.getUserId())
                        .bind("rating", review.getRating()).bind("content", review.getContent()).execute()
        );
        return affectedRow > 0;
    }

    public List<ProductReview> getReviewByProductId(int productId, PageRequest pageRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT rating,content,createdAt,userId FROM product_reviews ")
                .append("WHERE productId = :productId ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("productId", productId)
                        .mapToBean(ProductReview.class).stream().toList()
        );
    }

    public boolean deleteByProductId(int productId) throws Exception {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM product_reviews WHERE productId = :productId")
                        .bind("productId", productId)
                        .execute()
        );
        return result >= 0;
    }

}
