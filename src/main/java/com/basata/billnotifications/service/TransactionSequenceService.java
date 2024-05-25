package com.basata.billnotifications.service;

import com.basata.billnotifications.repo.ReceiptRepo;
import org.springframework.stereotype.Service;

@Service
public class TransactionSequenceService {

    ReceiptRepo receiptRepo;

    public TransactionSequenceService(ReceiptRepo receiptRepo) {
        this.receiptRepo = receiptRepo;
    }

    public String getReceiptNumber() {
        return receiptRepo.getReceiptNumber();
    }
}
