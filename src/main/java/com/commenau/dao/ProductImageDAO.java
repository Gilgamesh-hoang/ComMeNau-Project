package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.model.ProductImage;
import org.jdbi.v3.core.statement.Update;

import java.util.List;

public class ProductImageDAO {

    public String findAvatarByProductId(int productId) {
        String sql = "SELECT image FROM product_images WHERE productId = :productId AND isAvatar = :isAvatar";
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .bind("isAvatar", 1)
                        .mapTo(String.class).stream().findFirst().orElse(""));
    }

    public List<String> getAllImageByProductId(int id) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select image from product_images where productId = ?").bind(0, id).mapTo(String.class).stream().toList();
        });
    }


    public int save(ProductImage productImage) {
        String sql = "INSERT INTO product_images(productId,image,isAvatar) VALUES (:productId,:image,:isAvatar)";
        return JDBIConnector.getInstance().inTransaction(handle -> {
                    Update update = handle.createUpdate(sql).bindBean(productImage);
                    return update.bind("isAvatar", productImage.isAvatar() ? 1 : 0)
                            .executeAndReturnGeneratedKeys("id")
                            .mapTo(Integer.class).stream().findFirst().orElse(0);
                }
        );
    }

    public boolean deleteByProductId(int productId) throws Exception {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM product_images WHERE productId = :productId")
                        .bind("productId", productId)
                        .execute()
        );
        return result >= 0;

    }
}
