package com.basata.billnotifications.batch.processor;

import com.basata.billnotifications.model.ProcessedBillingData;
import com.basata.billnotifications.model.Transaction;
import com.basata.billnotifications.repo.TransactionRepo;
import com.basata.billnotifications.service.ResponseService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetInquiryResponseProcessor implements ItemProcessor<ProcessedBillingData, ProcessedBillingData> {

    private final TransactionRepo transactionRepo;

    private final ResponseService responseService;

    public GetInquiryResponseProcessor(TransactionRepo transactionRepo, ResponseService responseService) {
        this.transactionRepo = transactionRepo;
        this.responseService = responseService;
    }

    @Override
    public ProcessedBillingData process(ProcessedBillingData item) {
        Optional<Transaction> result = transactionRepo.findById(item.getTransactionId());
        if(result.isEmpty()) {
            return null;
        }

        if(result.get().getResponse() == null || result.get().getResponse().isEmpty()) {
            return null;
        }

        Double amount = responseService.getKeyDoubleFromObject(result.get().getResponse(), "amount");

        if(amount == null || amount == 0.0) {
            return null;
        }

        item.getBillingData().setResponse(result.get().getResponse());
        return item;
    }
}
