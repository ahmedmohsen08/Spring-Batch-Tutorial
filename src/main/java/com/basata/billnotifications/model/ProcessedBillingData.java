package com.basata.billnotifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedBillingData {
    private Long transactionId;
    private BillingData billingData;
}
