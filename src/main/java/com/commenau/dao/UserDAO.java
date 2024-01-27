package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.constant.SystemConstant;
import com.commenau.model.User;
import com.commenau.pagination.PageRequest;
import com.commenau.util.PagingUtil;

import java.util.List;
import java.util.Optional;

public class UserDAO {
    public boolean isAdmin(int id) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select count(*) from users join roles on users.roleId = roles.id where users.id = ? and roles.name = 'ROLE_ADMIN'").bind(0, id).mapTo(Integer.class).one() > 0;
        });
    }


    public User getUserByUsername(String username) {
        Optional<User> user = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE username = ?")
                        .bind(0, username).mapToBean(User.class).stream().findFirst()
        );
        return user.orElse(null);
    }

    public User getUserByEmail(String email) {
        Optional<User> user = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE email = ?")
                        .bind(0, email).mapToBean(User.class).stream().findFirst()
        );
        return user.orElse(null);
    }

    public User getUserById(Long id) {
        Optional<User> user = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE id = ?")
                        .bind(0, id).mapToBean(User.class).stream().findFirst());
        return user.orElse(null);
    }

    public boolean insert(User user) {
        String sql = "INSERT INTO users (roleId, firstName, lastName, email, username, password, phoneNumber, address, status ) " +
                "VALUES(:roleId, :firstName, :lastName, :email, :username, :password, :phoneNumber, :address, :status)";
        try {
            int result = JDBIConnector.getInstance().inTransaction(handle ->
                    handle.createUpdate(sql)
                            .bindBean(user)
                            .execute()
            );
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean activatedUser(int userId) {
        try {
            int result = JDBIConnector.getInstance().inTransaction(handle ->
                    handle.createUpdate("UPDATE users SET status = :status WHERE id = :id")
                            .bind("status", SystemConstant.ACTIVATED)
                            .bind("id", userId).execute());
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(User user) {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("UPDATE users SET password = :password WHERE id = :id")
                        .bindBean(user).execute());
        return result > 0;
    }

    public boolean updateProfile(User user) {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("UPDATE users SET firstName = :firstName, lastName = :lastName ," +
                        " phoneNumber= :phoneNumber, address = :address WHERE id = :id").bindBean(user).execute()
        );
        return result > 0;
    }

    public User getFirstNameAndLastName(Long userId) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select firstName , lastName from users where id = ?").bind(0, userId).mapToBean(User.class).stream().findFirst().orElse(null);
        });
    }

    public boolean lockOrUnlock(Long userId, String activated) {
        int row = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("UPDATE users SET status = :status WHERE id = :id")
                        .bind("status", activated).bind("id", userId).execute()
        );
        return row > 0;
    }

    public int countAll() {
        Optional<Integer> total = JDBIConnector.getInstance().withHandle(handle ->
                        handle.createQuery("SELECT COUNT(u.id) FROM users u INNER JOIN roles r ON u.roleId = r.id WHERE r.name = :roleName"))
                .bind("roleName", SystemConstant.USER).mapTo(Integer.class).stream().findFirst();
        return total.orElse(0);
    }

    public int countByKeyWord(String keyWord) {
        String sql = "SELECT COUNT(DISTINCT u.id) FROM users u INNER JOIN roles r ON u.roleId = r.id " +
                "WHERE r.name = :roleName AND (firstName like :keyWord or lastName like :keyWord or " +
                "username like :keyWord or phoneNumber like :keyWord or email like :keyWord or " +
                "address like :keyWord)";
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("keyWord", "%" + keyWord + "%")
                        .bind("roleName", SystemConstant.USER)
                        .mapTo(Integer.class).stream().findFirst().orElse(0)
        );
    }

    public List<User> findAll(PageRequest pageRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT users.* FROM users INNER JOIN roles ON roleId = ROLES.id WHERE roles.name = :roleName ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("roleName", SystemConstant.USER).mapToBean(User.class).stream().toList());
    }

    public List<User> findByKeyWord(PageRequest pageRequest, String keyWord) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT users.* FROM users INNER JOIN roles ON roleId = ROLES.id WHERE roles.name = :roleName ")
                .append("AND (firstName like :keyWord or lastName like :keyWord or email like :keyWord or ")
                .append("username like :keyWord or phoneNumber like :keyWord or address like :keyWord)");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("keyWord", "%" + keyWord + "%")
                        .bind("roleName", SystemConstant.USER).mapToBean(User.class).stream().toList());
    }
}
