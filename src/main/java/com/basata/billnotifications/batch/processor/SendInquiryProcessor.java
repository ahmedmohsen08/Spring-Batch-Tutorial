package com.basata.billnotifications.batch.processor;

import com.basata.billnotifications.model.BillingData;
import com.basata.billnotifications.model.InfoData;
import com.basata.billnotifications.model.ProcessedBillingData;
import com.basata.billnotifications.service.TransactionSequenceService;
import com.basata.billnotifications.util.TSUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemProcessor;
import sg.technobiz.client.enums.TransactType;
import sg.technobiz.client.objects.Transact;
import sg.technobiz.client.enums.InterfaceType;

public class SendInquiryProcessor implements ItemProcessor<BillingData, ProcessedBillingData> {

    private final TSUtil tsUtil;

    private final TransactionSequenceService transactionSequenceService;

    private final ObjectMapper mapper;

    public SendInquiryProcessor(TSUtil tsUtil, TransactionSequenceService transactionSequenceService, ObjectMapper mapper) {
        this.tsUtil = tsUtil;
        this.transactionSequenceService = transactionSequenceService;
        this.mapper = mapper;
    }

    @Override
    public ProcessedBillingData process(BillingData item) throws Exception {
        InfoData infoData = new InfoData();
        infoData.setSource("NotificationAutomation");
        infoData.setTitle("Bill Reminder");
        infoData.setContent("Pay your bills");
        infoData.setDeviceId(item.getDeviceId());

        String receiptId = transactionSequenceService.getReceiptNumber();
        Transact tr = new Transact();
        tr.setFrom_account(item.getUserAccount());// sender
        tr.setTo_account(item.getServiceAccount());// receiver
        tr.setUser_id(item.getCustomerId());
        tr.setAmount(0d);
        tr.setTotal_amount(0d);
        tr.setType(TransactType.PAYMENT);
        tr.setInterface_type(InterfaceType.WEB);
        tr.setQuantity(1);
        tr.setReceipt_id(receiptId);
        tr.setRequest(item.getRequest());
        tr.setSession_id("00000000-0000-0000-0000-000000000000");
        tr.setClient_id(item.getClientId());
        tr.setInfo(mapper.writeValueAsString(infoData));

        Long trId = tsUtil.sendTransaction(tr);
        return new ProcessedBillingData(trId, item);
    }
}
