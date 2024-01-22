package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.model.ProductImage;
import org.jdbi.v3.core.statement.Update;

import java.util.List;
import java.util.Optional;

public class ProductImageDAO {
    public ProductImage getFirstImageById(int productId) {
        Optional<ProductImage> re = JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery("select image from product_images where productId = ?")
                    .bind(0, productId)
                    .mapToBean(ProductImage.class)
                    .stream().findFirst();
        });
        return re.orElse(null);
    }

    public String findAvatarByProductId(int productId) {
        String sql = "SELECT image FROM product_images WHERE productId = :productId AND isAvatar = :isAvatar";
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .bind("isAvatar", 1)
                        .mapTo(String.class).one());
    }

    public List<String> getAllImageByProductId(int id) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select image from product_images where productId = ?").bind(0, id).mapTo(String.class).stream().toList();
        });
    }

    public String getAvatar(int productId) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select image from product_images where productId = ? && isAvatar = 1").bind(0, productId).mapTo(String.class).findOne().orElse("");
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
