package com.basata.billnotifications.repo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sg.technobiz.client.objects.PushNotification;

@Repository
public class PushNotificationRepo {
    JdbcTemplate jdbcTemplate;
    public PushNotificationRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertPushNotification(PushNotification pushNotification) {
        String sql = "INSERT INTO pushnotification (user_id, device_id, title, content) VALUES (?, ?::uuid, ?, ?)";
        return jdbcTemplate.update(sql,
                pushNotification.getUser_id(),
                pushNotification.getDevice_id(),
                pushNotification.getTitle(),
                pushNotification.getContent());
    }
}
