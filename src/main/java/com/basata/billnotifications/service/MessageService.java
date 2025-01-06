package com.basata.billnotifications.service;

import com.basata.billnotifications.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.technobiz.client.objects.Message;
import sg.technobiz.client.objects.PushNotification;

@Service
public class MessageService {
    private final MessageRepo messageRepo;

    @Autowired
    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public void saveMessage(Message message) {
        messageRepo.insertMessage(message);
    }

    public Long getMessageId() {
        return messageRepo.getMessageId();
    }
}
