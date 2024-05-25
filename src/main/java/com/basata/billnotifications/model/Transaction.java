package com.basata.billnotifications.model;


import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.technobiz.client.enums.InterfaceType;
import sg.technobiz.client.enums.TransactStatus;
import sg.technobiz.client.enums.TransactType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "transact")

public class Transaction implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String receiptId;
    private Long fromAccount;
    private Long toAccount;
    private Double amount;
    private Double totalAmount;
    private String clientId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "interface_type")
    private InterfaceType interfaceType;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactType type;
    private String info;
    private Long refId;
    private Integer quantity;
    private Integer printedCount;
    private String sessionId;
    private String request;
    @Column(columnDefinition = "jsonb")
    private String response;
    private Date createTimestamp;
    private Date updateTimestamp;
}
