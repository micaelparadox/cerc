package com.cerc.tio.libcdpcommon.pubsub.publisher;

import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionDomainType;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionRequest;
import com.cerc.tio.libcdpcommon.pubsub.publisher.exception.MessagePublishingException;
import com.cerc.tio.libcdpcommon.pubsub.publisher.exception.PublisherCreationException;
import com.google.api.core.ApiFuture;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.Map;

@Setter
@Slf4j
public class BookkeepingPublisher {
    private DefaultPublisher publisher;

    /**
     * Creates a new BookkeepingPublisher with the default topic id "prioritizer.new".
     * @param projectId The project id of the Google Cloud project
     * @throws PublisherCreationException If the publisher fails to be created
     */
    public BookkeepingPublisher(String projectId) throws PublisherCreationException {
        String topicId = "prioritizer.new";
        publisher = new DefaultPublisher(projectId, topicId);
    }

    /**
     * Creates a new BookkeepingPublisher with the specified topic id.
     * @param projectId The project id of the Google Cloud project
     * @param topicId The topic id of the Google Cloud Pub/Sub topic
     * @throws PublisherCreationException If the publisher fails to be created
     */
    public BookkeepingPublisher(String projectId, String topicId) throws PublisherCreationException {
        publisher = new DefaultPublisher(projectId, topicId);
    }

    /**
     * Publishes a message to the bookkeeping topic asynchronously.
     * @param trace A trace id to be used in logs
     * @param transactionRequest The specific bookkeeping transaction request to be published
     * @param domainType The domain type of the message. This will be used as an attribute in the message.
     */
    public ApiFuture<String> publishAsync(String trace, BKTransactionRequest transactionRequest, BKTransactionDomainType domainType) {
        String message = new JSONObject(transactionRequest).toString();
        Map<String, String> attributes = Map.of("domain", domainType.getDomain());

        return publisher.publishAsync(trace, message, attributes);
    }

    /**
     * Publishes a message to the bookkeeping topic synchronously.
     * @param trace A trace id to be used in logs
     * @param transactionRequest The JSON serialized message to be published
     * @param domainType The domain type of the message. This will be used as an attribute in the message.
     * @return The message id of the published message
     * @throws InterruptedException If the thread is interrupted while waiting for the message to be published
     */
    public String publishSync(String trace, BKTransactionRequest transactionRequest, BKTransactionDomainType domainType) throws InterruptedException, MessagePublishingException {
        String message = new JSONObject(transactionRequest).toString();
        Map<String, String> attributes = Map.of("domain", domainType.getDomain());

        return publisher.publishSync(trace, message, attributes);
    }
}
