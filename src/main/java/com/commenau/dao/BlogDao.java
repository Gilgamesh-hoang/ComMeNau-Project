package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.model.Blog;
import com.commenau.paging.PageRequest;
import com.commenau.util.PagingUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BlogDao {
    public Blog findOneById(int id) {
        Optional<Blog> blog = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT * FROM blogs WHERE id = ?").bind(0, id).mapToBean(Blog.class).stream().findFirst()
        );
        return blog.orElse(null);
    }

    public List<Blog> findByNewest(int limit) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT id, title, image, shortDescription, createdAt FROM blogs " +
                                "ORDER BY createdAt DESC limit :limit").bind("limit", limit)
                        .mapToBean(Blog.class).list()
        );
    }

    public int countAll() {
        int blogCount = JDBIConnector.getInstance().withHandle(handle -> {
            return handle.createQuery("SELECT COUNT(id) FROM blogs")
                    .mapTo(Integer.class)
                    .one();
        });

        return blogCount;
    }


    public int countByKeyWord(String keyWord) {
        String sql = "SELECT COUNT(id) FROM blogs WHERE title LIKE :keyWord OR shortDescription LIKE :keyWord OR content LIKE :keyWord";
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("keyWord", "%" + keyWord + "%")
                        .mapTo(Integer.class)
                        .stream().findFirst().orElse(0)
        );
    }

    public List<Blog> findAll(PageRequest pageRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT blogs.*, COUNT(br.id) AS numReviews FROM blogs ")
                .append("LEFT JOIN blog_reviews br ON blogs.id = br.blogId GROUP BY blogs.id ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).mapToBean(Blog.class).stream().toList());
    }

    public List<Blog> findByKeyWord(PageRequest pageRequest, String keyWord) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT blogs.*, COUNT(br.id) AS numReviews FROM blogs ")
                .append("LEFT JOIN blog_reviews br ON blogs.id = br.blogId ")
                .append("WHERE title LIKE :keyWord OR shortDescription LIKE :keyWord OR blogs.content LIKE :keyWord ")
                .append("GROUP BY blogs.id ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("keyWord", "%" + keyWord + "%")
                        .mapToBean(Blog.class).stream().toList());
    }

    public boolean delete(int id) throws Exception {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM blogs WHERE id = :id")
                        .bind("id", id).execute()
        );
        return result > 0;
    }

    public boolean save(Blog blog) {
        String sql = "INSERT INTO blogs(id,title,shortDescription,content,image,createdBy) VALUES " +
                "(:id,:title,:shortDescription,:content,:image,:createdBy)";
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate(sql).bindBean(blog).execute());
        return result > 0;
    }

    public boolean update(Blog blog) {
        String sql;
        if (blog.getImage() == null)
            sql = "UPDATE blogs SET title=:title,shortDescription=:shortDescription,content=:content WHERE id=:id";
        else
            sql = "UPDATE blogs SET title=:title,shortDescription=:shortDescription,content=:content,image=:image WHERE id=:id";

        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate(sql).bindBean(blog).execute());
        return result > 0;
    }


}
