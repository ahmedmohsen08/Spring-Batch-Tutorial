package com.basata.billnotifications.service;

import com.basata.billnotifications.model.InfoData;
import com.basata.billnotifications.model.PushNotification;
import com.basata.billnotifications.repo.PushNotificationRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.impossibl.postgres.api.jdbc.PGConnection;
//import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sg.technobiz.client.enums.PGListenerTopic;
import sg.technobiz.client.objects.Transact;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

import javax.sql.DataSource;
import java.sql.*;

@Service
@Slf4j
public class PGNotificationService1 implements Runnable {

    ObjectMapper mapper;

    JdbcTemplate jdbcTemplate;

    private final DataSource dataSource;

    private final PushNotificationRepo pushNotificationRepo;

    @Value("${spring.datasource.url.pg}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    PGConnection connection;

    public PGNotificationService1(ObjectMapper mapper, JdbcTemplate jdbcTemplate, DataSource dataSource, PushNotificationRepo pushNotificationRepo) throws SQLException {
        this.mapper = mapper;
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.pushNotificationRepo = pushNotificationRepo;
    }

    public void subscribe() {
        try {
            Connection connection = dataSource.getConnection();
            log.info("Subscribe to {} events......", PGListenerTopic.PAYMENT_RESULT);
            PreparedStatement statement = connection.prepareStatement("LISTEN " + PGListenerTopic.PAYMENT_RESULT.getKey());
            statement.execute();
            statement.close();
            log.info("Subscribed to {} events.", PGListenerTopic.PAYMENT_RESULT);

            while (true) {
                PGConnection pgConnection = connection.unwrap(PGConnection.class);
                org.postgresql.PGNotification[] notifications = pgConnection.getNotifications();

                if (notifications != null) {
                    for (PGNotification notification : notifications) {
                        Transact update = mapper.readValue(notification.getParameter(), Transact.class);
                        if (update == null || update.getAmount() == 0 || update.getInfo().isBlank()) {
                            return;
                        }

                        InfoData infoData = mapper.readValue(update.getInfo(), InfoData.class);
                        if (infoData == null || !infoData.getSource().equals("NotificationAutomation")) return;

                        PushNotification pushNotification = new PushNotification();
                        pushNotification.setUserId(update.getUser_id());
                        pushNotification.setTitle(infoData.getTitle());
                        pushNotification.setContent(infoData.getContent());
                        pushNotification.setDeviceId(infoData.getDeviceId());

                        pushNotificationRepo.save(pushNotification);
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Database exception", e);
        } catch (JsonProcessingException ex) {
                        log.error("PGNotifyException: ", ex);
                        throw new RuntimeException(ex);
                    }
    }

    public void unsubscribe() {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("LISTEN " + PGListenerTopic.PAYMENT_RESULT.getKey());
            statement.execute();
            statement.close();
            connection.close();
            log.info("Unsubscribed from payment result notifications.");
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

    @Override
    public void run() {
        subscribe();
    }
}
