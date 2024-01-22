package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.model.Category;
import com.commenau.paging.PageRequest;

import java.util.List;
import java.util.Optional;

public class CategoryDAO {
    public String getNameById(int id) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select name from categories where id = ?").bind(0, id).mapTo(String.class).one();
        });
    }

    public String getCategoryNameById(int productId) {

        Optional<Category> re = JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery("select c.name from categories c join products p on c.id = p.categoryId where p.id = ?")
                    .bind(0, productId)
                    .mapToBean(Category.class).stream().findFirst();
        });
        return re.orElse(null).getName();
    }

    public List<Category> findAll(PageRequest pageRequest) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM categories ORDER BY id DESC");
        if (pageRequest.getMaxPageItem() != 0)
            sql.append(" LIMIT " + pageRequest.getOffset() + "," + pageRequest.getMaxPageItem());
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql.toString()).mapToBean(Category.class).stream().toList());
    }

    public int totalItem() {
        int total = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT COUNT(id) FROM categories").mapTo(Integer.class).one());
        return total;
    }

    public boolean update(Category category) {
        int result = JDBIConnector.getInstance().withHandle(handle ->
                        handle.createUpdate("UPDATE categories SET name=:name, code=:code WHERE id=:id"))
                .bindBean(category).execute();
        return result > 0;
    }

    public boolean save(Category category) {
        int result = JDBIConnector.getInstance().withHandle(handle ->
                        handle.createUpdate("INSERT INTO categories(name, code) VALUES(:name,:code)"))
                .bindBean(category).execute();
        return result > 0;
    }

    public boolean delete(Integer id) throws Exception {
        String sql = "DELETE FROM categories WHERE id =:id";
        int rowsDeleted = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate(sql).bind("id", id).execute());
        return rowsDeleted > 0;
    }


    public List<Category> getAllCategory() {
        return JDBIConnector.getInstance().withHandle(n -> {
           return n.createQuery("select id , name from categories").mapToBean(Category.class).stream().toList();
        });
    }
}
