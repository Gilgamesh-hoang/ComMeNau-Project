package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.dto.ContactDTO;
import com.commenau.model.Contact;
import com.commenau.model.ReplyContact;
import com.commenau.pagination.PageRequest;
import com.commenau.util.PagingUtil;
import org.jdbi.v3.core.statement.Update;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ContactDAO {
    public int countAll() {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT COUNT(id) FROM contacts")
                        .mapTo(Integer.class).one()
        );
    }

    public int countByKeyWord(String keyWord) {
        String sql = "SELECT COUNT(id) FROM contacts WHERE fullName LIKE :keyWord " +
                "OR email LIKE :keyWord OR message LIKE :keyWord OR CONVERT(createdAt, CHAR) LIKE :keyWord";
        return JDBIConnector.getInstance().withHandle(handle ->
                        handle.createQuery(sql))
                .bind("keyWord", "%" + keyWord + "%")
                .mapTo(Integer.class).stream().findFirst().orElse(0);
    }
    public ReplyContact findReplyByContactId(int contactId) {
        String sql = "SELECT * FROM reply_contacts WHERE contactId=:contactId";
        Optional<ReplyContact> contact = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("id", contactId)
                        .mapToBean(ReplyContact.class).stream().findFirst());
        return contact.orElse(null);
    }

    public Contact findOneById(int contactId) {
        String sql = "SELECT c.id AS id, fullName, email, message " +
                "FROM contacts c LEFT JOIN reply_contacts r ON r.contactId = c.id WHERE c.id=:id";
        Optional<Contact> contact = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("id", contactId)
                        .mapToBean(Contact.class).stream().findFirst());
        return contact.orElse(new Contact());
    }
    public List<ContactDTO> findAll(PageRequest pageRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT c.id AS contactId, fullName, email, message, createdAt, r.id AS replyId ");
        builder.append("FROM contacts c LEFT JOIN reply_contacts r ON r.contactId = c.id ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).map((rs, ctx) -> {
                    int replyId = rs.getInt("replyId");
                    ReplyContact reply = (replyId == 0) ? null : ReplyContact.builder().id(replyId).build();
                    return ContactDTO.builder()
                            .id(rs.getInt("contactId"))
                            .fullName(rs.getString("fullName"))
                            .email(rs.getString("email"))
                            .message(rs.getString("message"))
                            .createdAt(rs.getTimestamp("createdAt"))
                            .reply(reply).build();
                }).stream().toList());
    }

    public List<ContactDTO> findByKeyWord(PageRequest pageRequest, String keyWord) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT c.id AS contactId, fullName, email, message,createdAt, r.id AS replyId ");
        builder.append("FROM contacts c LEFT JOIN reply_contacts r ON r.contactId = c.id ");
        builder.append("WHERE fullName LIKE :keyWord OR email LIKE :keyWord OR message LIKE :keyWord ");
        builder.append("OR CONVERT(createdAt, CHAR) LIKE :keyWord ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("keyWord", "%" + keyWord + "%")
                        .map((rs, ctx) -> {
                            int replyId = rs.getInt("replyId");
                            ReplyContact reply = (replyId == 0) ? null : ReplyContact.builder().id(replyId).build();
                            return ContactDTO.builder()
                                    .id(rs.getInt("contactId"))
                                    .fullName(rs.getString("fullName"))
                                    .email(rs.getString("email"))
                                    .message(rs.getString("message"))
                                    .createdAt(rs.getTimestamp("createdAt"))
                                    .reply(reply).build();
                        }).stream().toList());
    }

    public boolean save(Contact contact) {
        String sql = "INSERT INTO contacts(fullName, email, message, userId) VALUES (:fullName, :email, :message, :userId)";
        int result = JDBIConnector.getInstance().inTransaction(handle -> {
            Update update = handle.createUpdate(sql)
                    .bind("fullName", contact.getFullName())
                    .bind("email", contact.getEmail())
                    .bind("message", contact.getMessage());
            if (contact.getUserId() != 0) {
                update.bind("userId", contact.getUserId());
            } else {
                update.bindNull("userId", Types.INTEGER);
            }
            return update.execute();
        });
        return result > 0;
    }

    public boolean saveReply(ReplyContact replyContact) {
        String sql = "INSERT INTO reply_contacts(contactId, title, content) VALUES (:contactId,:title,:content)";
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("contactId", replyContact.getContactId())
                        .bind("title", replyContact.getTitle())
                        .bind("content", replyContact.getContent()).execute()
        );
        return result > 0;
    }

    public boolean deleteByIds(Integer[] ids) {
        int result = JDBIConnector.getInstance().inTransaction(handle -> {
            return handle.createUpdate("DELETE FROM contacts WHERE id IN (<ids>)")
                    .bindList("ids", Arrays.asList(ids))
                    .execute();

        });
        return result > 0;
    }


    public boolean deleteReplyByContactId(Integer contactId) {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM reply_contacts WHERE contactId =:contactId")
                        .bind("contactId", contactId)
                        .execute()

        );
        return result > 0;
    }

    public boolean deleteById(Integer id) {
        int result = JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("DELETE FROM contacts WHERE id =:id")
                        .bind("id", id)
                        .execute()

        );
        return result > 0;
    }


}
