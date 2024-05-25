//package com.basata.billnotifications.service;
//
//import com.basata.billnotifications.model.InfoData;
//import com.basata.billnotifications.repo.PushNotificationRepo;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.impossibl.postgres.api.jdbc.PGConnection;
//import com.impossibl.postgres.api.jdbc.PGNotificationListener;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//import sg.technobiz.client.enums.PGListenerTopic;
//import com.basata.billnotifications.model.PushNotification;
//import sg.technobiz.client.objects.Transact;
//
//import javax.sql.DataSource;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//@Service
//@Slf4j
//public class PGNotificationService {
//
//    ObjectMapper mapper;
//
//    JdbcTemplate jdbcTemplate;
//
//    private final DataSource dataSource;
//
//    private final PushNotificationRepo pushNotificationRepo;
//
//    @Value("${spring.datasource.url.pg}")
//    private String url;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    PGConnection connection;
//
//    public PGNotificationService(ObjectMapper mapper, JdbcTemplate jdbcTemplate, DataSource dataSource, PushNotificationRepo pushNotificationRepo) throws SQLException {
//        this.mapper = mapper;
//        this.jdbcTemplate = jdbcTemplate;
//        this.dataSource = dataSource;
//        this.pushNotificationRepo = pushNotificationRepo;
//        initPGConnection();
//    }
//
//    public void initPGConnection() throws SQLException {
////        Connection connection = dataSource.getConnection();
////        if (connection.isWrapperFor(PGConnection.class)) {
////            return connection.unwrap(PGConnection.class);
////        } else {
////            throw new SQLException("Connection is not a PostgreSQL connection.");
////        }
//
////        DataSource dataSource = jdbcTemplate.getDataSource();
////        PGConnection connection = dataSource.getConnection().unwrap(PGConnection.class);
////        return connection;
//
//    }
//
//    public void subscribe() {
//        try {
//            connection = DriverManager.getConnection(url, username, password).unwrap(PGConnection.class);
//            connection.addNotificationListener(PGListenerTopic.PAYMENT_RESULT.getKey(), new PGNotificationListener() {
//
//                @Override
//                public void notification(int processId, String channelName, String payload) {
//                    try {
//                        log.info("inside subscribe notification");
//                        Transact update = mapper.readValue(payload, Transact.class);
//                        if (update == null || update.getAmount() == 0 || update.getInfo().isBlank()) {
//                            return;
//                        }
//
//                        InfoData infoData = mapper.readValue(update.getInfo(), InfoData.class);
//                        if (infoData == null || !infoData.getSource().equals("NotificationAutomation")) return;
//
//                        PushNotification pushNotification = new PushNotification();
//                        pushNotification.setUserId(update.getUser_id());
//                        pushNotification.setTitle(infoData.getTitle());
//                        pushNotification.setContent(infoData.getContent());
//                        pushNotification.setDeviceId(infoData.getDeviceId());
//
//                        pushNotificationRepo.save(pushNotification);
//                    } catch (JsonProcessingException ex) {
//                        log.error("PGNotifyException: ", ex);
//                        throw new RuntimeException(ex);
//                    }
//                }
//            });
//            log.info("Subscribe to {} events......", PGListenerTopic.PAYMENT_RESULT);
//            Statement stmt = connection.createStatement();
//            stmt.executeUpdate("LISTEN " + PGListenerTopic.PAYMENT_RESULT.getKey());
//            stmt.close();
//            log.info("Subscribed to {} events.", PGListenerTopic.PAYMENT_RESULT);
//        } catch (SQLException e) {
//            log.error("Database exception", e);
//        }
//    }
//
//    public void unsubscribe() {
//        try {
//            Statement stmt = connection.createStatement();
//            stmt.executeUpdate("UNLISTEN " + PGListenerTopic.PAYMENT_RESULT.getKey());
//            log.info("Unsubscribed from payment result notifications.");
//            connection.removeNotificationListener(PGListenerTopic.PAYMENT_RESULT.getKey());
//            connection.close();
//        } catch (Exception e) {
//            log.error("Exception", e);
//        }
//    }
//}
