package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.model.InvoiceItem;
import com.commenau.model.Product;
import com.commenau.util.RoundUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InvoiceItemDAO {

    public List<InvoiceItem> getAllInvoiceItemById(int invoiceId) {
        String sql = "select sum(ii.price) + i.shippingFee from invoice_items ii JOIN invoices i ON ii.invoiceId = i.id where invoiceId = ?";
        String sql1 = "select productId,quantity, price from invoice_items where invoiceId = ?";
        return JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery(sql1)
                    .bind(0, invoiceId)
                    .mapToBean(InvoiceItem.class)
                    .list();
        });
    }

    public Integer totalPrice(Integer invoiceId) {
        String sql = "SELECT SUM(price * quantity) FROM invoice_items WHERE invoiceId = ?";
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind(0, invoiceId)
                        .mapTo(Integer.class).stream().findFirst().orElse(0)

        );
    }

    public InvoiceItem getBestSellingProduct() {
        Optional<InvoiceItem> invoiceItem = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT productId, SUM(quantity) AS quantity FROM invoice_items ii " +
                                "INNER JOIN invoices i ON i.id = ii.invoiceId " +
                                "WHERE YEAR(i.createdAt) = YEAR(CURDATE()) AND MONTH(i.createdAt) = MONTH(CURDATE()) " +
                                "GROUP BY productId ORDER BY quantity DESC LIMIT 1 ")
                        .mapToBean(InvoiceItem.class).stream().findFirst()
        );
        return invoiceItem.orElse(new InvoiceItem());
    }

    public boolean transferProductsFromCartToInvoice(Long userId, int invoiceId) {
        String sql = "INSERT INTO invoice_items (invoiceId, productId, quantity, price) " +
                "SELECT :invoiceId, productId, quantity, ROUND((price * (1 - discount)) /1000)*1000 " +
                "FROM cart_items ci " +
                "INNER JOIN carts c ON c.id = ci.cartId " +
                "INNER JOIN products p ON ci.productId = p.id " +
                "WHERE c.userId = :userId ";
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("userId", userId)
                        .bind("invoiceId", invoiceId).execute());
        return result > 0;
    }

    public boolean save(int invoiceId, Map<Product, Integer> map) {
        String sql = "INSERT INTO invoice_items(invoiceId,productId,quantity,price) " +
                "VALUES (:invoiceId,:productId,:quantity,:price)";
        try {
            JDBIConnector.getInstance().inTransaction(handle -> {
                for (Map.Entry<Product, Integer> entry : map.entrySet()) {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();
                    double price = RoundUtil.roundPrice(product.getPrice() * (1 - product.getDiscount()));
                    int result = handle.createUpdate(sql)
                            .bind("invoiceId", invoiceId)
                            .bind("productId", product.getId())
                            .bind("quantity", quantity)
                            .bind("price", price)
                            .execute();
                    if (result == 0)
                        return false;
                }
                return true;
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProductId(int productId) throws Exception {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("UPDATE invoice_items SET productId=NULL WHERE productId = :productId")
                        .bind("productId", productId)
                        .execute()
        );
        return result >= 0;
    }
}
