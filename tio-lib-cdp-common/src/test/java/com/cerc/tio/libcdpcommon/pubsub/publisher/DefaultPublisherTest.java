package com.cerc.tio.libcdpcommon.pubsub.publisher;

import com.cerc.tio.libcdpcommon.pubsub.publisher.exception.MessagePublishingException;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.PubsubMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultPublisherTest {

    private final DefaultPublisher underTest = new DefaultPublisher("project-id", "topic-id");

    @Mock
    private Publisher publisher;

    @BeforeEach
    void setUp() {
        underTest.setPublisher(publisher);
    }


    @Test
    void publishAsync_shouldPublishMessageCorrectly() throws ExecutionException, InterruptedException, IOException {
        // Given
        String trace = "trace";
        String message = "{\"field\": \"value\"}";
        Map<String, String> attributes = Map.of("type", "test");

        //When
        when(publisher.publish(any(PubsubMessage.class))).thenReturn(ApiFutures.immediateFuture("messageId"));
        ApiFuture<String> future = underTest.publishAsync(trace, message, attributes);

        //Then
        assertEquals("messageId", future.get());
    }

    @Test
    void publishAsync_shouldThrowException_whenExceptionIsThrownWhilePublishingMessage() {
        // Given
        String trace = "trace";
        String message = "{\"field\": \"value\"}";
        Map<String, String> attributes = Map.of("type", "test");

        //When
        when(publisher.publish(any(PubsubMessage.class))).thenThrow(new RuntimeException());

        //Then
        assertThrows(MessagePublishingException.class, () -> underTest.publishAsync(trace, message, attributes));
    }

    @Test
    void publishSync_shouldPublishMessageCorrectly() throws InterruptedException {
        // Given
        String trace = "trace";
        String message = "{\"field\": \"value\"}";
        Map<String, String> attributes = Map.of("type", "test");

        //When
        when(publisher.publish(any(PubsubMessage.class))).thenReturn(ApiFutures.immediateFuture("messageId"));
        String messageId = underTest.publishSync(trace, message, attributes);

        //Then
        assertEquals("messageId", messageId);
    }

    @Test
    void publishSync_shouldThrowException_whenExceptionIsThrownWhilePublishingMessage() throws InterruptedException {
        // Given
        String trace = "trace";
        String message = "{\"field\": \"value\"}";
        Map<String, String> attributes = Map.of("type", "test");

        //When
        when(publisher.publish(any(PubsubMessage.class))).thenThrow(new CancellationException());

        //Then
        assertThrows(MessagePublishingException.class, () -> underTest.publishSync(trace, message, attributes));
    }

    @Test
    void buildPubsubMessage_shouldBuildMessageCorrectly() {
        String message = "{\"field\": \"value\"}";
        Map<String, String> attributes = Map.of("type", "test");

        PubsubMessage pubsubMessage = underTest.buildPubsubMessage(message, attributes);

        assertEquals("{\"field\": \"value\"}", message);
        assertEquals("test", attributes.get("type"));
    }

    @Test
    void buildPubsubMessage_shouldThrowException_whenMessageIsNotJson() {
        String message = "not a json message";
        Map<String, String> attributes = Map.of("type", "test");

        assertThrows(IllegalArgumentException.class, () -> underTest.buildPubsubMessage(message, attributes));
    }
}