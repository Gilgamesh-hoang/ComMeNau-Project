package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.model.Category;
import com.commenau.paging.PageRequest;

import java.util.List;

public class CategoryDAO {
    public String getNameById(int id) {
        return JDBIConnector.getInstance().withHandle(n ->
                n.createQuery("SELECT name FROM categories WHERE id = ?").bind(0, id).mapTo(String.class)
                        .stream().findFirst().orElse(null)
        );
    }

    public List<Category> findAll(PageRequest pageRequest) {
        String sql = "SELECT * FROM categories ORDER BY id DESC";
        if (pageRequest.getMaxPageItem() != 0)
            sql += " LIMIT " + pageRequest.getOffset() + "," + pageRequest.getMaxPageItem();
        String finalSql = sql;
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(finalSql).mapToBean(Category.class).stream().toList());
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
        return JDBIConnector.getInstance().withHandle(n ->
                n.createQuery("SELECT id , name FROM categories").mapToBean(Category.class).list()
        );
    }
}
