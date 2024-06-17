package com.cerc.tio.libcdpcommon.pubsub.consumer;

import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSubscriber.class);

    private final Subscriber subscriber;

    public MessageSubscriber(
        final String projectId,
        final String subscriptionName,
        final int maxMessages,
        final MessageReceiver messageReceiver
    ) {
        LOGGER.info("Creating subscriber for project: {}, subscription: {}", projectId, subscriptionName);
        this.subscriber = Subscriber.newBuilder(
                ProjectSubscriptionName.of(projectId, subscriptionName),
                messageReceiver
            )
            .setParallelPullCount(maxMessages)
            .setExecutorProvider(
                InstantiatingExecutorProvider.newBuilder()
                    .setExecutorThreadCount(maxMessages)
                    .build()
            ).build();
    }

    public void start() {
        LOGGER.info("Starting listening messages from sub {}", subscriber.getSubscriptionNameString());
        this.subscriber.startAsync().awaitRunning();
    }

    public void stop() {
        LOGGER.info("Stopping listening messages from sub {}", subscriber.getSubscriptionNameString());
        this.subscriber.stopAsync().awaitTerminated();
    }
}
