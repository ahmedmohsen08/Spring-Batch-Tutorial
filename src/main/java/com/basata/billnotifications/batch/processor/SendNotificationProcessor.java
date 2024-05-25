package com.basata.billnotifications.batch.processor;

import com.basata.billnotifications.model.ProcessedBillingData;
import com.basata.billnotifications.service.PushNotificationService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import sg.technobiz.client.objects.PushNotification;

@Component
public class SendNotificationProcessor implements ItemProcessor<ProcessedBillingData, ProcessedBillingData> {

    private final PushNotificationService pushNotificationService;

    public SendNotificationProcessor(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @Override
    public ProcessedBillingData process(ProcessedBillingData item) throws Exception {
        PushNotification pushNotification = new PushNotification();
        pushNotification.setTitle("You have unpaid bills");
        pushNotification.setContent("Pay your bills for " + item.getBillingData().getProviderName() + " - " + item.getBillingData().getServiceName());
        pushNotification.setUser_id(item.getBillingData().getCustomerId());
        pushNotification.setDevice_id(item.getBillingData().getDeviceId().toString());

        pushNotificationService.savePushNotification(pushNotification);
        return item;
    }
}
