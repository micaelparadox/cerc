package com.cerc.tio.libcdpcommon.pubsub.consumer;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.pubsub.v1.PubsubMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

    @Mock
    private MessageProcessor messageProcessor;

    @InjectMocks
    private MessageConsumer underTest;

    @Test
    void receiveMessage_Should_ack_message_after_processed() {
        // Given
        AckReplyConsumer ackReplyConsumer = mock(AckReplyConsumer.class);
        PubsubMessage pubsubMessage = mock(PubsubMessage.class);

        // When
        doNothing().when(ackReplyConsumer).ack();
        when(pubsubMessage.getMessageId()).thenReturn("testId");
        underTest.receiveMessage(pubsubMessage, ackReplyConsumer);

        // Then
        verify(ackReplyConsumer, times(1)).ack();
    }

    @Test
    void receiveMessage_Should_nack_message_when_exception_is_thrown() {
        // Given
        AckReplyConsumer ackReplyConsumer = mock(AckReplyConsumer.class);
        PubsubMessage pubsubMessage = mock(PubsubMessage.class);
        doThrow(new RuntimeException("test")).when(messageProcessor).process(any(PubsubMessage.class));

        // When
        doNothing().when(ackReplyConsumer).nack();
        when(pubsubMessage.getMessageId()).thenReturn("testId");
        underTest.receiveMessage(pubsubMessage, ackReplyConsumer);

        // Then
        verify(ackReplyConsumer, times(1)).nack();
    }

}