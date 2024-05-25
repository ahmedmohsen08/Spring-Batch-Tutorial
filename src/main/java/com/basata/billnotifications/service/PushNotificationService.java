package com.basata.billnotifications.service;

import com.basata.billnotifications.repo.PushNotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.technobiz.client.objects.PushNotification;

@Service
public class PushNotificationService {

    private final PushNotificationRepo pushNotificationService;

    @Autowired
    public PushNotificationService(PushNotificationRepo pushNotificationRepo) {
        this.pushNotificationService = pushNotificationRepo;
    }

    public void savePushNotification(PushNotification pushNotification) {
        pushNotificationService.insertPushNotification(pushNotification);
    }
}
