package com.basata.billnotifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedBillingData implements Serializable {
    private Long transactionId;
    private BillingData billingData;
}
