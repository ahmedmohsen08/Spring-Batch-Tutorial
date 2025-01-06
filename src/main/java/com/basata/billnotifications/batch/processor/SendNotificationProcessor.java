package com.basata.billnotifications.batch.processor;

import com.basata.billnotifications.model.ProcessedBillingData;
import com.basata.billnotifications.service.MessageService;
import com.basata.billnotifications.service.PushNotificationService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import sg.technobiz.client.enums.MessagePriority;
import sg.technobiz.client.enums.MessageType;
import sg.technobiz.client.objects.Message;
import sg.technobiz.client.objects.PushNotification;

@Component
public class SendNotificationProcessor implements ItemProcessor<ProcessedBillingData, ProcessedBillingData> {

    private final PushNotificationService pushNotificationService;
    private final MessageService messageService;

    public SendNotificationProcessor(PushNotificationService pushNotificationService, MessageService messageService) {
        this.pushNotificationService = pushNotificationService;
        this.messageService = messageService;
    }

    @Override
    public ProcessedBillingData process(ProcessedBillingData item) throws Exception {
        String subject = "You have unpaid bills";
        String title = "Pay your bills for " + item.getBillingData().getProviderName() + " - " + item.getBillingData().getServiceName();
        Long messageId = messageService.getMessageId();

        Message message = new Message();
        message.setId(messageId);
        message.setSender(0L);
        message.setReceiver(item.getBillingData().getCustomerId());
        message.setSubject(subject);
        message.setContent(title);
        message.setType(MessageType.INFO);
        message.setPriority(MessagePriority.NORMAL);

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTitle(subject);
        pushNotification.setContent(title);
        pushNotification.setUser_id(item.getBillingData().getCustomerId());
        pushNotification.setDevice_id(item.getBillingData().getDeviceId().toString());
        pushNotification.setExtras("type=message||id=" + messageId + "||token_id=" + item.getBillingData().getCustomerId() + "||date='||CURRENT_DATE");

        pushNotificationService.savePushNotification(pushNotification);
        return item;
    }
}
