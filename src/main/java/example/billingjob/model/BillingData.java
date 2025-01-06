package example.billingjob.model;

import java.util.UUID;

public record BillingData(
        Long transactionId,
        Long customerId,
        String receiptId,
        String request,
        String clientId,
        String serviceName,
        String providerName,
        UUID deviceId
) {
}
