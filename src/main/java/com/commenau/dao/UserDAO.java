package com.commenau.dao;

import com.commenau.constant.SystemConstant;
import com.commenau.model.User;

import java.util.List;
import java.util.Optional;
import com.commenau.connectionPool.ConnectionPool;
public class UserDAO {
    public boolean isAdmin(int id) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery("select count(*) from users join roles on users.roleId = roles.id where users.id = ? and roles.name = 'ROLE_ADMIN'").bind(0, id).mapTo(Integer.class).one() > 0;
        });
    }


    public User getUserByUsername(String username) {
        Optional<User> user = ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE username = ?")
                        .bind(0, username).mapToBean(User.class).stream().findFirst()
        );
        return user.orElse(null);
    }

    public User getUserByEmail(String email) {
        Optional<User> user = ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE email = ?")
                        .bind(0, email).mapToBean(User.class).stream().findFirst()
        );
        return user.orElse(null);
    }

    public User getUserById(Long id) {
        Optional<User> user = ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery("select * from users where id = ?")
                        .bind(0, id).mapToBean(User.class).stream().findFirst());
        return user.orElse(null);
    }

    public boolean insert(User user) {
        String sql = "INSERT INTO users (roleId, firstName, lastName, email, username, password, phoneNumber, address, status ) " +
                "VALUES(:roleId, :firstName, :lastName, :email, :username, :password, :phoneNumber, :address, :status)";
        try {
            int result = ConnectionPool.getConnection().inTransaction(handle ->
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
            int result = ConnectionPool.getConnection().inTransaction(handle ->
                    handle.createUpdate("UPDATE users SET status = :status WHERE id = :id")
                            .bind("status", SystemConstant.ACTIVATED)
                            .bind("id", userId)
                            .execute());
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(User user) {
        try {
            int result = ConnectionPool.getConnection().inTransaction(handle ->
                    handle.createUpdate("UPDATE users SET password = :password WHERE id = :id")
                            .bindBean(user).execute());
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProfile(User user) {
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate("UPDATE users SET firstName = :firstName" +
                                ", lastName = :lastName , phoneNumber= :phoneNumber, address = :address " +
                                "WHERE id = :id")
                        .bindBean(user)
                        .execute()
        );
        return result > 0;
    }

    public User getFirstNameAndLastName(Long userId) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery("select firstName , lastName from users where id = ?").bind(0, userId).mapToBean(User.class).stream().findFirst().orElse(null);
        });
    }

    public List<User> getAllCustomerPaged(int pageIndex, int pageSize) {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select id,firstName, lastName, username, email, phoneNumber, address, status from users where roleId = 1 " +
                            "order by firstName asc limit ? offset ?")
                    .bind(0, pageSize)
                    .bind(1, (pageIndex - 1) * pageSize)
                    .mapToBean(User.class)
                    .list();
        });
    }

    public boolean changeStatusOfCustomer(User user) {
        try {
            int result = ConnectionPool.getConnection().inTransaction(handle ->
                    handle.createUpdate("UPDATE users set status = ? where id = ?")
                            .bind(0, user.getStatus())
                            .bind(1, user.getId())
                            .execute());
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean lockOrUnlock(Long userId, String activated) {
        int row = ConnectionPool.getConnection().inTransaction(handle -> {
            return handle.createUpdate("update users set status = :status where id = :id")
                    .bind("status", activated)
                    .bind("id", userId)
                    .execute();
        });
        return row > 0;
    }

    public List<User> findUserByInput(String input) {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select id,firstName, lastName, username, email, phoneNumber, address, status from users " +
                            "where (firstName like :input or " +
                            "lastName like :input or " +
                            "username like :input or " +
                            "phoneNumber like :input or " +
                            "email like :input or " +
                            "address like :input) and " +
                            "roleId = 1")
                    .bind("input", "%" + input + "%")
                    .mapToBean(User.class)
                    .list();
        });
    }

    public List<User> getAllCustomer() {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select id from users where roleId = 1")
                    .mapToBean(User.class)
                    .list();
        });
    }

    public int countAll() {
        Optional<Integer> total = ConnectionPool.getConnection().withHandle(handle ->
                        handle.createQuery("SELECT COUNT(u.id) FROM users u INNER JOIN roles r ON u.roleId = r.id WHERE r.name = :name"))
                .bind("name", SystemConstant.USER).mapTo(Integer.class).stream().findFirst();
        return total.orElse(0);
    }
}
