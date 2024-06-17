package com.cerc.tio.libcdpcommon.pubsub.consumer;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor(staticName = "of")
public class MessageConsumer implements MessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    private final MessageProcessor messageProcessor;

    @Override
    public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
        try {
            LOGGER.info("Received message {}", pubsubMessage.getMessageId());
            this.messageProcessor.process(pubsubMessage);
            ackReplyConsumer.ack();
        } catch (Exception e) {
            ackReplyConsumer.nack();
            LOGGER.error("Nacking message {} due to error", pubsubMessage.getMessageId(), e);
        }
    }

}