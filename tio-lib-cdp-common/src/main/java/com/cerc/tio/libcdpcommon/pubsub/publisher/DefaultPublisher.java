package com.cerc.tio.libcdpcommon.pubsub.publisher;

import com.cerc.tio.libcdpcommon.pubsub.publisher.exception.MessagePublishingException;
import com.cerc.tio.libcdpcommon.pubsub.publisher.exception.PublisherCreationException;
import com.cerc.tio.libcdpcommon.pubsub.publisher.extension.ApiFuturesCalbackImpl;
import com.cerc.tio.libcdpcommon.pubsub.publisher.factory.PublisherFactory;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.cerc.tio.libcdpcommon.util.JSONUtil.isValidJson;

@Slf4j
@Getter
public class DefaultPublisher {
    private final String topicId;
    private final String projectId;

    @Setter
    private Publisher publisher;

    protected static final String SYNC_SUCCESS_MESSAGE = "[{}] Message published successfully with id [{}]!";
    protected static final String ASYNC_SUCCESS_MESSAGE = "[{}] Message sent to be published to topic {} on project {}!";
    protected static final String FAILED_MESSAGE = "[{}] Failed to publish message!";

    public DefaultPublisher(String projectId, String topicId) throws PublisherCreationException {
        this.projectId = projectId;
        this.topicId = topicId;
        try {
            this.publisher = PublisherFactory.createPublisher(projectId, topicId);
        } catch (Exception e) {
            throw new PublisherCreationException("Failed to create publisher!", e);
        }
    }

    /**
     * Publishes a message to the topic asynchronously.
     * @param trace A trace id to be used in logs
     * @param message The JSON serialized message to be published
     * @param attributes The attributes to be added to the message
     */
    public ApiFuture<String> publishAsync(String trace, String message, Map<String, String> attributes) throws MessagePublishingException {
        PubsubMessage pubsubMessage = buildPubsubMessage(message, attributes);
        ApiFuture<String> future;

        try {
            future = publisher.publish(pubsubMessage);
            log.debug(ASYNC_SUCCESS_MESSAGE, trace, topicId, projectId);

            ApiFutures.addCallback(future, new ApiFuturesCalbackImpl(trace), MoreExecutors.directExecutor());
        } catch (Exception e) {
            log.error(FAILED_MESSAGE, trace, e);
            throw new MessagePublishingException("Failed to publish message!", e);
        }

        return future;
    }

    /**
     * Publishes a message to the topic synchronously.
     * @param message The JSON serialized message to be published
     * @param attributes The attributes to be added to the message
     * @return The message id of the published message
     * @throws InterruptedException If the thread is interrupted while waiting for the message to be published
     */
    public String publishSync(String trace, String message, Map<String, String> attributes) throws InterruptedException, MessagePublishingException {
        PubsubMessage pubsubMessage = buildPubsubMessage(message, attributes);
        String messageId;

        try {
            messageId = publisher.publish(pubsubMessage).get();
            log.debug(SYNC_SUCCESS_MESSAGE, trace, messageId);
        } catch (InterruptedException ie) {
            log.error(FAILED_MESSAGE, trace, ie);
            throw ie;
        } catch (Exception e) {
            log.error(FAILED_MESSAGE, trace, e);
            throw new MessagePublishingException("Error while publishing message!", e);
        }

        return messageId;
    }

    /**
     * Builds a PubsubMessage from a JSON serialized message and attributes.
     * @param message The JSON serialized message
     * @param attributes The attributes to be added to the message
     * @return The built PubsubMessage object
     */
    PubsubMessage buildPubsubMessage(String message, Map<String, String> attributes) {
        if (!isValidJson(message)) {
            throw new IllegalArgumentException("Message is not a valid JSON!");
        }

        return PubsubMessage.newBuilder()
                .setData(ByteString.copyFromUtf8(message))
                .putAllAttributes(attributes)
                .build();
    }
}
