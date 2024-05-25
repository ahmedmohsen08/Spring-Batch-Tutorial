package com.basata.billnotifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingData implements Serializable {
        private Long transactionId;
        private Long customerId;
        private Long userAccount;
        private Long serviceAccount;
        private String receiptId;
        private String request;
        private String response;
        private String clientId;
        private String serviceName;
        private String providerName;
        private UUID deviceId;
}
