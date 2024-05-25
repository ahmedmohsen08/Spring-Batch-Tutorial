package com.basata.billnotifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BillNotificationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillNotificationsApplication.class, args);
    }

}
