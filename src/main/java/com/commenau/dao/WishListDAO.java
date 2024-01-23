package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.model.WishlistItem;

import java.util.List;

public class WishListDAO {
    public boolean existsItem(int productId, int userId) {
        int result = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM wishlists WHERE userId = ? AND productId = ? ")
                        .bind(0, userId).bind(1, productId)
                        .mapTo(Integer.class).stream().findFirst().orElse(0));
        return result > 0;
    }

    public void saveItem(int userID, int productId) {
        JDBIConnector.getInstance().inTransaction(handle ->
            handle.createUpdate("INSERT INTO wishlists(userID,productId) VALUES (?,?)")
                    .bind(0, userID).bind(1, productId).execute()
        );
    }

    public boolean deleteItem(long userID, int productId) {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM wishlists WHERE userId = ? AND productId = ? ")
                        .bind(0, userID).bind(1, productId).execute()
        );
        return result > 0;
    }


    public boolean deleteByProductId(int productId) throws Exception {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM wishlists WHERE productId = :productId")
                        .bind("productId", productId)
                        .execute()
        );
        return result > 0;
    }

    public boolean resetAll(Long userId) {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM wishlists WHERE userId = :userId")
                        .bind("userId", userId)
                        .execute()
        );
        return result > 0;
    }

    public List<WishlistItem> findWishListByUser(Long userId) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT productId FROM wishlists WHERE userId = ?")
                        .bind(0, userId).mapToBean(WishlistItem.class).list()
        );
    }
}
