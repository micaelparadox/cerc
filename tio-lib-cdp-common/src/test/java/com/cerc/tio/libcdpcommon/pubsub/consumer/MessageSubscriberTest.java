package com.cerc.tio.libcdpcommon.pubsub.consumer;

import com.google.api.core.ApiService;
import com.google.cloud.pubsub.v1.Subscriber;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class MessageSubscriberTest {

    @Test
    void start_Should_start_subscriber_When_called() throws IllegalAccessException {
        // Given
        MessageSubscriber underTest = mock(MessageSubscriber.class);
        Subscriber subscriber = mock(Subscriber.class);
        ApiService apiService = mock(ApiService.class);

        when(subscriber.startAsync()).thenReturn(apiService);
        doNothing().when(apiService).awaitRunning();
        FieldUtils.writeField(underTest, "subscriber", subscriber, true);

        doCallRealMethod().when(underTest).start();

        // When
        underTest.start();

        // Then
        verify(subscriber, times(1)).startAsync();
        verify(apiService, times(1)).awaitRunning();
    }

    @Test
    void stop_Should_stop_subscriber_When_called() throws IllegalAccessException {
        // Given
        MessageSubscriber underTest = mock(MessageSubscriber.class);
        Subscriber subscriber = mock(Subscriber.class);
        ApiService apiService = mock(ApiService.class);

        when(subscriber.stopAsync()).thenReturn(apiService);
        doNothing().when(apiService).awaitTerminated();
        FieldUtils.writeField(underTest, "subscriber", subscriber, true);

        doCallRealMethod().when(underTest).stop();

        // When
        underTest.stop();

        // Then
        verify(subscriber, times(1)).stopAsync();
        verify(apiService, times(1)).awaitTerminated();
    }

}