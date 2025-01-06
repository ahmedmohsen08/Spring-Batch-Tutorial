package com.basata.billnotifications.repo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sg.technobiz.client.objects.Message;
import sg.technobiz.client.objects.PushNotification;

@Repository
public class MessageRepo {
    JdbcTemplate jdbcTemplate;
    public MessageRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertMessage(Message message) {
        String sql = "insert into message (id, sender, receiver, subject, content, type, priority) values(?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                message.getId(),
                message.getSender(),
                message.getReceiver(),
                message.getSubject(),
                message.getContent(),
                message.getType(),
                message.getPriority());
    }

    public Long getMessageId() {
        String sql = "select NEXTVAL('message_id_seq')";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
