package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.model.Cart;
import com.commenau.model.CartItem;

import java.util.List;
import java.util.Optional;

public class CartDAO {
    public List<CartItem> findCartItemByUserId(long userId) {
        String sql = "SELECT ci.id AS id, ci.quantity, ci.productId FROM carts AS c "+
                "INNER JOIN cart_items AS ci ON c.id = ci.cartId WHERE c.userId = :userId";
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(CartItem.class).list()
        );
    }

    public Cart findCartByUserId(long userId) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT * FROM carts WHERE userId = :userId")
                        .bind("userId", userId)
                        .mapToBean(Cart.class)
                        .one()
        );
    }

    public Cart findOneById(int id) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT * FROM carts WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(Cart.class)
                        .one());
    }

    public CartItem findCartItemByProductAndCart(int productId, int cartId) {
        String sql = "SELECT * FROM cart_items WHERE productId = :productId AND cartId=:cartId";
        Optional<CartItem> first = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .bind("cartId", cartId)
                        .mapToBean(CartItem.class)
                        .findFirst());
        return first.orElse(null);
    }

    public Cart save(Cart cart) {
        int cartId = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("INSERT INTO carts(userId) VALUES(:userId)")
                        .bind("userId", cart.getUserId())
                        .executeAndReturnGeneratedKeys().mapTo(Integer.class).one());
        return findOneById(cartId);
    }

    public boolean saveCartItem(CartItem item) {
        String sql = "INSERT INTO cart_items(cartId, productId, quantity) VALUES(:cartId, :productId, :quantity)";
        Integer result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("cartId", item.getCartId())
                        .bind("productId", item.getProductId())
                        .bind("quantity", item.getQuantity())
                        .execute());
        return result > 0;
    }

    public boolean updateCartItem(CartItem itemEntity, long userId) {
        Cart cart = findCartByUserId(userId);
        String sql = "UPDATE cart_items SET quantity =:quantity WHERE productId=:productId AND cartId = :cartId";
        Integer result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("productId", itemEntity.getProductId())
                        .bind("quantity", itemEntity.getQuantity())
                        .bind("cartId", cart.getId())
                        .execute());
        return result > 0;
    }

    public boolean deleteProduct(int productId, long userId) {
        Cart cart = findCartByUserId(userId);
        String sql = "DELETE FROM cart_items WHERE productId = :productId AND cartId = :cartId";
        int result = JDBIConnector.getInstance().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("cartId", cart.getId())
                        .bind("productId", productId)
                        .execute());
        return result > 0;
    }

    public boolean deleteByProductId(int productId) throws Exception {
        try {
            int result = JDBIConnector.getInstance().inTransaction(handle ->
                    handle.createUpdate("DELETE FROM cart_items WHERE productId = :productId")
                            .bind("productId", productId)
                            .execute()
            );
            return result >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAll(long userId) {
        String sql = "DELETE ci FROM cart_items ci INNER JOIN carts c ON c.id = ci.cartId WHERE c.userId = :userId";
        int result = JDBIConnector.getInstance().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("userId", userId)
                        .execute());
        return result > 0;

    }


}
