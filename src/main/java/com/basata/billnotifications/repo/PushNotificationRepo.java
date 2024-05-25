package com.basata.billnotifications.repo;

import com.basata.billnotifications.model.PushNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushNotificationRepo extends JpaRepository<PushNotification, Long> {
}
