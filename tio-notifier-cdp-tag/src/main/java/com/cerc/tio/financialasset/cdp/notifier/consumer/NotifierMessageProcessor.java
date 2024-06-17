package com.cerc.tio.financialasset.cdp.notifier.consumer;

import com.cerc.tio.financialasset.cdp.notifier.router.NotificationRouter;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationMessage;
import com.cerc.tio.libcdpcommon.pubsub.consumer.MessageConsumer;
import com.cerc.tio.libcdpcommon.pubsub.consumer.MessageProcessor;
import com.cerc.tio.libcdpcommon.pubsub.consumer.MessageSubscriber;
import com.google.pubsub.v1.PubsubMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class NotifierMessageProcessor implements MessageProcessor {
    @Value("${app.messaging.notifier-sub-project}")
    private String notifierSubProject;
    @Value("${app.messaging.notifier-sub}")
    private String notifierSubName;
    @Value("${app.messaging.notifier-sub-max-messages}")
    private int notifierSubMaxMessages;

    @Autowired
    private NotificationRouter notificationRouter;
    @Autowired
    private ObjectMapper objectMapper;



    @Bean(initMethod = "start", destroyMethod = "stop")
    public MessageSubscriber receiveNotifierMessages() {
        return new MessageSubscriber(
            notifierSubProject, notifierSubName, notifierSubMaxMessages, MessageConsumer.of(this)
        );
    }

    @Override
    public void process(PubsubMessage pubsubMessage) {
        try{
            String  messageData = pubsubMessage.getData().toStringUtf8();
            BKNotificationMessage message = objectMapper.readValue(messageData, BKNotificationMessage.class);
            notificationRouter.route(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
