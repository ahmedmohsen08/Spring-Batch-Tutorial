package com.basata.billnotifications.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.technobiz.client.enums.PushNotificationStatus;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "pushnotification")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pushnotification_id_seq")
    @SequenceGenerator(sequenceName = "pushnotification_id_seq", schema = "public", name = "pushnotification_id_seq", allocationSize = 1)
    private Long id;
    private Long userId;
    private UUID deviceId;
    private String title;
    private String content;
    private String extras;
    private PushNotificationStatus status;
    private Long batchId;
    private Date createTimestamp;
    private Date updateTimestamp;
}
