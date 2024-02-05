package com.commenau.dao;

import com.commenau.connectionPool.JDBIConnector;
import com.commenau.model.Conversation;
import com.commenau.model.Message;
import com.commenau.pagination.PageRequest;
import com.commenau.util.PagingUtil;
import org.jdbi.v3.core.statement.Update;

import java.util.List;

public class ConversationDAO {
    public int findConservationById(int participantId) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT id FROM conversations WHERE participantId = ?")
                        .bind(0, participantId).mapTo(Integer.class).stream().findFirst().orElse(0)
        );
    }

    public Message findMessageById(int messageId) {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT * FROM messages WHERE id = ?")
                        .bind(0, messageId).mapToBean(Message.class).stream().findFirst().orElse(null)
        );
    }

    public int createConversation(int participantId) {
        return JDBIConnector.getInstance().inTransaction(handle ->
                handle.createUpdate("INSERT INTO conversations(participantId) VALUES (:participantId)")
                        .bind("participantId", participantId).executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class).stream().findFirst().orElse(0)
        );
    }

    public Message saveMessage(Message message) {
        int id = JDBIConnector.getInstance().inTransaction(handle -> {
            Update update = handle.createUpdate("INSERT INTO messages(conversationId,senderId,recipientId,content,viewed) " +
                            "VALUES (:conversationId,:senderId,:recipientId,:content,:viewed)")
                    .bindBean(message);
            update.bind("viewed", message.isViewed() ? 1 : 0);
            return update.executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class).stream().findFirst().orElse(0);
        });
        return this.findMessageById(id);

    }

    public List<Message> getMessages(int participantId, PageRequest pageRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT senderId,viewed,content,sendTime FROM messages ")
                .append("INNER JOIN conversations ON messages.conversationId = conversations.id ")
                .append("WHERE participantId = :participantId ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery(sql).bind("participantId", participantId).mapToBean(Message.class).stream().toList()
        );
    }

    public Message getLastMessage(int participantId) {
        return JDBIConnector.getInstance().withHandle(n -> {
            return n.createQuery("select senderId , viewed , content , sendTime from messages join conversations on messages.conversationId = conversations.id where participantId = ? order by sendTime desc limit 1")
                    .bind(0, participantId).mapToBean(Message.class).stream().findFirst().orElse(null);
        });
    }

    public boolean updateViewed(int participantId, int ownerId) {
        String getMessageSql = "SELECT id,senderId FROM messages WHERE conversationId=:conversationId ORDER BY sendTime DESC LIMIT 1";
        String updateViewedSql = "UPDATE messages SET viewed = 1 WHERE id = :id";
        int affectedRows = JDBIConnector.getInstance().inTransaction(handle -> {
            int conversationId = this.findConservationById(participantId);

            Message message = handle.createQuery(getMessageSql).bind("conversationId", conversationId)
                    .mapToBean(Message.class).stream().findFirst().orElse(new Message());

            if (message.getSenderId() != ownerId) {
                return handle.createUpdate(updateViewedSql).bind("id", message.getId()).execute();
            }
            return 0;
        });
        return affectedRows > 0;
    }

    public List<Conversation> getAllConversations() {
        return JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT * FROM conversations").mapToBean(Conversation.class).stream().toList()
        );
    }


}
