package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.constant.SystemConstant;
import com.commenau.dto.ProductDTO;
import com.commenau.model.Product;
import com.commenau.paging.PageRequest;
import com.commenau.util.PagingUtil;
import org.jdbi.v3.core.statement.Update;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProductDAO {

    public Product findOneById(int productId) {
        String sql = "SELECT id,categoryId,name,description,price,discount,status,rate,available FROM products WHERE id = :id";
        Product product = JDBIConnector.getInstance(). withHandle(handle ->
                handle.createQuery(sql).bind("id", productId).mapToBean(Product.class).stream().findFirst().orElse(new Product()));
        return product;
    }


    public int averageRating(int id) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select avg(rating) from product_reviews where productId = ?").bind(0, id).mapTo(Double.class).findOne().orElse(0d).intValue();
        });
    }

    public int countProductReviewsById(int id) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select count(*) from product_reviews where productId = ?").bind(0, id).mapTo(Integer.class).findOne().orElse(0);
        });
    }


    public List<Product> getRelativeProductViewByID(int productID) {
        return JDBIConnector.getInstance().withHandle(n -> {
            int categoryId = n.createQuery("select categoryId from products where id = ? and status = 1").bind(0, productID).mapTo(Integer.class).one();
            return n.createQuery("select id, categoryId ,description , name , price , discount from products where categoryId = ? order by createdAt desc limit 4").bind(0, categoryId).mapToBean(Product.class).stream().toList();
        });
    }

    public List<Product> getAllIdAndName() {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select id , name from products where  status = 1").mapToBean(Product.class).stream().toList();
        });
    }

    public List<Product> getProductViewPage(int categoryId, int size, int page, String sortBy, String sort) {
        if (sortBy.equals("price")){
            sortBy = "price_discount";
        }
        String sortby = sortBy;
        return JDBIConnector.getInstance().withHandle(n -> {
            String sql = null;

            if (categoryId == 0) {
                sql = "select *, p.price * (1 - p.discount) AS price_discount from products as p where  status = 1  order by " + sortby + " " + sort + "  limit ? offset ?";
                return n.createQuery(sql).bind(0, size).bind(1, (page - 1) * size).mapToBean(Product.class).stream().toList();
            } else {
                sql = "select *, p.price * (1 - p.discount) AS price_discount from products as p where  status = 1 and p.categoryId =  ? order by " + sortby + " " + sort + "  limit ? offset ?";
                return n.createQuery(sql).bind(0, categoryId).bind(1, size).bind(2, (page - 1) * size).mapToBean(Product.class).stream().toList();
            }
        });
    }

    public List<Product> getProductViewPage(int categoryId, int size, int page) {
        return JDBIConnector.getInstance().withHandle(n -> {
            String sql = null;
            if (categoryId == 0) {
                sql = "select p.id, p.createdAt , p.categoryId, p.name , p.description , p.price , p.discount , p.status , p.available, avg(pr.rating) as rate from products  as p left join product_reviews as pr on p.id = pr.productId where p.status = 1 group by p.id   limit ? offset ?";
                return n.createQuery(sql).bind(0, size).bind(1, (page - 1) * size).mapToBean(Product.class).stream().toList();
            } else {
                sql = "select p.id, p.createdAt , p.categoryId, p.name , p.description , p.price , p.discount , p.status , p.available, avg(pr.rating) as rate from products  as p left join product_reviews as pr on p.id = pr.productId where p.status = 1 and p.categoryId =  ? group by p.id   limit ? offset ?";
                return n.createQuery(sql).bind(0, categoryId).bind(1, size).bind(2, (page - 1) * size).mapToBean(Product.class).stream().toList();
            }
        });
    }

    public List<Product> getProductViewPageSortByRating(int categoryId, int size, int page, String sort) {
        return JDBIConnector.getInstance().withHandle(n -> {
            String sql = null;
            if (categoryId == 0) {
                sql = "select p.id, p.createdAt,p.name , p.categoryId, p.description , p.price , p.discount, p.available , p.status, avg(pr.rating) as rate from products  as p left join product_reviews as pr on p.id = pr.productId where p.status = 1 group by p.id   order by rating  " + sort + "  limit ? offset ? ";
                return n.createQuery(sql).bind(0, size).bind(1, (page - 1) * size).mapToBean(Product.class).stream().toList();
            } else {
                sql = "select p.id, p.createdAt,p.name , p.categoryId, p.description , p.price , p.discount, p.available , p.status, avg(pr.rating) as rate from products  as p left join product_reviews as pr on p.id = pr.productId where p.status = 1 and p.categoryId =  ? group by p.id   order by rating  " + sort + "  limit ? offset ? ";
                return n.createQuery(sql).bind(0, categoryId).bind(1, size).bind(2, (page - 1) * size).mapToBean(Product.class).stream().toList();
            }
        });
    }

    public int countProductsInCategory(int categoryId) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select count(*) from products where status = 1 and categoryId = ?").bind(0, categoryId).mapTo(Integer.class).findOne().orElse(0);
        });
    }


    public List<Product> getNewRelativeProductView() {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select id, categoryId ,description , name , price , discount from products where status = 1 and categoryId = 1 order by createdAt desc limit 8").mapToBean(Product.class).stream().toList();
        });
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

    public List<ProductDTO> findAll(PageRequest pageRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p.id AS productId,c.name AS categoryName,p.name AS productName,price,status,rate,available,image ");
        builder.append("FROM products p ");
        builder.append("LEFT JOIN categories c ON p.categoryId = c.id ");
        builder.append("LEFT JOIN product_images pi ON p.id = pi.productId AND pi.isAvatar = 1 ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> {
                            return ProductDTO.builder()
                                    .id(rs.getInt("productId"))
                                    .categoryName(rs.getString("categoryName"))
                                    .name(rs.getString("productName"))
                                    .price(rs.getDouble("price"))
                                    .status(rs.getBoolean("status"))
                                    .rate((int) rs.getFloat("rate"))
                                    .available(rs.getInt("available"))
                                    .avatar(rs.getString("image") == null ? "" : rs.getString("image"))
                                    .build();
                        }).stream().toList()
        );
    }

    public List<Product> findByKeyWord(PageRequest pageRequest, String keyWord) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p.id AS id, p.name AS name,c.id AS categoryId, price,status,rate,available ");
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
