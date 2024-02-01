package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.constant.SystemConstant;
import com.commenau.model.Product;
import com.commenau.pagination.PageRequest;
import com.commenau.pagination.Sorter;
import com.commenau.util.PagingUtil;
import org.jdbi.v3.core.statement.Update;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProductDAO {

    public Product findOneById(int productId) {
        String sql = "SELECT id,categoryId,name,description,price,discount,status,available FROM products WHERE id = :id";
        Product product = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("id", productId).mapToBean(Product.class).stream().findFirst().orElse(new Product()));
        return product;
    }


    public int averageRating(int id) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("SELECT AVG(rating) FROM product_reviews WHERE productId = ?")
                    .bind(0, id).mapTo(Double.class).findOne().orElse(0d).intValue();
        });
    }

    public int countProductReviewsById(int id) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("SELECT COUNT(id) FROM product_reviews WHERE productId = ?" +
                    "").bind(0, id).mapTo(Integer.class).findOne().orElse(0);
        });
    }


    public List<Product> findRelativeProductsById(int productID) {
        String getCategoryIdSQL = "SELECT categoryId FROM products WHERE id = :id and status = :status";
        String getRelativeProducts = "SELECT id,categoryId,description,name,price,discount FROM products " +
                "WHERE categoryId = ? ORDER BY createdAt DESC limit 4";
        return JDBIConnector.getInstance().withHandle(handle -> {
                    int categoryId = handle.createQuery(getCategoryIdSQL)
                            .bind("id", productID).bind("status", SystemConstant.IN_BUSINESS)
                            .mapTo(Integer.class).stream().findFirst().orElse(0);
                    return handle.createQuery(getRelativeProducts).bind(0, categoryId).mapToBean(Product.class).stream().toList();
                }
        );
    }

    public List<Product> searchByName(String productName) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT id,name FROM products WHERE status = :status AND name LIKE :productName")
                        .bind("status", SystemConstant.IN_BUSINESS).bind("productName", "%" + productName + "%")
                        .mapToBean(Product.class).stream().toList()
        );
    }

    public int countProductsInCategory(int categoryId) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT COUNT(id) FROM products WHERE status = :status AND categoryId = :categoryId")
                        .bind("status", SystemConstant.IN_BUSINESS).bind("categoryId", categoryId)
                        .mapTo(Integer.class).findOne().orElse(0)
        );
    }


    public List<Product> findNewestProducts(int limit) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT id,categoryId,description,name,price,discount " +
                                "FROM products WHERE status = :status ORDER BY createdAt DESC limit :limit")
                        .bind("status", SystemConstant.IN_BUSINESS).bind("limit", limit)
                        .mapToBean(Product.class).stream().toList()
        );
    }

    public int countAll() {
        return JDBIConnector.getInstance().withHandle(handle ->
                        handle.createQuery("SELECT COUNT(id) FROM products"))
                .mapTo(Integer.class).stream().findFirst().orElse(0);
    }

    public int countByKeyWord(String keyWord) {
        String sql = "SELECT COUNT(DISTINCT p.id) FROM products p LEFT JOIN categories c ON p.categoryId = c.id " +
                "WHERE c.name LIKE :keyWord OR p.name LIKE :keyWord OR CONVERT(price, CHAR) LIKE :keyWord OR CONVERT(available, CHAR) LIKE :keyWord";
        return JDBIConnector.getInstance().withHandle(handle ->
                        handle.createQuery(sql))
                .bind("keyWord", "%" + keyWord + "%")
                .mapTo(Integer.class).stream().findFirst().orElse(0);
    }

    public List<Product> findAll(PageRequest pageRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM products ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Product.class).stream().toList()
        );
    }

    public List<Product> findByCategoryId(int categoryId, PageRequest pageRequest) {
        for (Sorter sorter : pageRequest.getSorters()) {
            if (sorter.getSortName().equalsIgnoreCase("price")) {
                sorter.setSortName("price_discount");
                break;
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p.*, price * (1 - discount) AS price_discount, AVG(pr.rating) AS rate ")
                .append("FROM products p LEFT JOIN product_reviews pr ON p.id = pr.productId ")
                .append("WHERE status = :status AND categoryId = :categoryId GROUP BY p.id ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("status", SystemConstant.IN_BUSINESS).bind("categoryId", categoryId)
                        .mapToBean(Product.class).stream().toList()
        );
    }

    public List<Product> findByKeyWord(PageRequest pageRequest, String keyWord) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p.id AS id, p.name AS name,c.id AS categoryId, price,status,available ");
        builder.append("FROM products p LEFT JOIN categories c ON p.categoryId = c.id ");
        builder.append("WHERE c.name LIKE :keyWord OR p.name LIKE :keyWord ");
        builder.append("OR CONVERT(price, CHAR) LIKE :keyWord OR CONVERT(available, CHAR) LIKE :keyWord ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("keyWord", "%" + keyWord + "%")
                        .mapToBean(Product.class).stream().toList()
        );
    }

    public int save(Product product) {
        String sql = "INSERT INTO products(name,description,price,discount,status,available,categoryId) " +
                "VALUES (:name,:description,:price,:discount,:status,:available,:categoryId)";
        try {
            return JDBIConnector.getInstance().inTransaction(handle -> {
                        Update update = handle.createUpdate(sql).bindBean(product);
                        return update.bind("status", product.isStatus() ? SystemConstant.IN_BUSINESS : SystemConstant.STOP_BUSINESS)
                                .executeAndReturnGeneratedKeys("id")
                                .mapTo(Integer.class).stream().findFirst().orElse(-1);
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int update(Product product) {
        String sql = "UPDATE products SET name=:name, description=:description, price=:price " +
                ",discount=:discount, status=:status, available=:available, categoryId=:categoryId " +
                "WHERE id=:id";
        try {
            return JDBIConnector.getInstance().inTransaction(handle -> {
                        Update update = handle.createUpdate(sql).bindBean(product);
                        return update.bind("status", product.isStatus() ? SystemConstant.IN_BUSINESS : SystemConstant.STOP_BUSINESS)
                                .execute();
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean deleteById(int productId) throws Exception {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM products WHERE id = :productId")
                        .bind("productId", productId)
                        .execute()
        );
        return result > 0;
    }


    public void setAvailableToZero() {
        try {
            JDBIConnector.getInstance().inTransaction(handle ->
                    handle.createUpdate("UPDATE products SET available = 0 WHERE status = :status AND available > 0")
                            .bind("status", SystemConstant.IN_BUSINESS)
                            .execute()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setDefaultAvailable() {
        return JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("UPDATE products SET available = 60 WHERE status = :status ")
                        .bind("status", SystemConstant.IN_BUSINESS)
                        .execute()
        ) > 0;
    }

    public boolean setDefaultAvailable(Integer[] ids) {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("UPDATE products SET available = 60 WHERE status = :status AND id IN (<ids>)")
                        .bindList("ids", Arrays.asList(ids))
                        .bind("status", SystemConstant.IN_BUSINESS)
                        .execute()
        );
        return result > 0;
    }

    public void updateAvailable(Map<Product, Integer> map) {
        JDBIConnector.getInstance().inTransaction(handle -> {
            map.keySet().forEach(key -> {
                handle.createUpdate("UPDATE products SET available = available - :quantity WHERE id = :id")
                        .bind("quantity", map.get(key))
                        .bind("id", key.getId())
                        .execute();
            });

            return null;
        });
    }

    public boolean updateCategory(Integer oldCategoryId, Integer newCategoryId) throws Exception {
        String sql = "UPDATE products SET categoryId = :newCategoryId WHERE categoryId = :oldCategoryId";
        int result = JDBIConnector.getInstance().inTransaction(handle -> {
            Update update = handle.createUpdate(sql).bind("oldCategoryId", oldCategoryId);
            if (newCategoryId != null) {
                update.bind("newCategoryId", newCategoryId);
            } else {
                update.bindNull("newCategoryId", Types.INTEGER);
            }
            return update.execute();
        });
        return result > 0;
    }


}
