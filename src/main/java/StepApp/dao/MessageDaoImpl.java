package StepApp.dao;

import StepApp.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {

    private final AppDao appDao;

    public MessageDaoImpl() {
        this.appDao = new AppDaoImpl();
    }

    @Override
    public void createMessageTable() {
        String createTableSql =
                        """
                        CREATE TABLE messages (
                        id SERIAL PRIMARY KEY,
                        src VARCHAR(255) NOT NULL,
                        dst VARCHAR(255) NOT NULL,
                        message TEXT NOT NULL,
                        sent BIGINT NOT NULL,
                        CONSTRAINT fk_src FOREIGN KEY (src) REFERENCES users(email),
                        CONSTRAINT fk_dst FOREIGN KEY (dst) REFERENCES users(email)
                        );
                        """;
        appDao.createTable(createTableSql, "messages");
    }

    @Override
    public boolean saveMessage(String src, String dst, String message, Long sent) {
        String insertQuery =
                """
                        INSERT INTO messages (src, dst, message, sent)
                        VALUES (?, ?, ?, ?)
                        """;
        return appDao.executeUpdate(insertQuery, stmt -> {
            stmt.setString(1, src);
            stmt.setString(2, dst);
            stmt.setString(3, message);
            stmt.setLong(4, sent);
        });
    }

    @Override
    public List<Message> getMessagesBetweenUsers(String user1, String user2) {
        String selectQuery =
                """
                        SELECT id, src, dst, message, sent
                        FROM messages
                        WHERE (src = ? AND dst = ?) OR (src = ? AND dst = ?)
                        ORDER BY sent ASC
                        """;

        return appDao.executeQuery(selectQuery,
                stmt -> {
                    stmt.setString(1, user1);
                    stmt.setString(2, user2);
                    stmt.setString(3, user2);
                    stmt.setString(4, user1);
                },
                rs -> {
                    List<Message> messages = new ArrayList<>();
                    while (rs.next()) {
                        messages.add(new Message(
                                rs.getLong("id"),
                                rs.getString("src"),
                                rs.getString("dst"),
                                rs.getString("message"),
                                rs.getLong("sent")
                        ));
                    }
                    return messages;
                });
    }
}
