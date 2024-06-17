package com.cerc.tio.financialasset.cdp.notifier.consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.cerc.tio.financialasset.cdp.notifier.router.NotificationRouter;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationMessage;
import com.cerc.tio.libcdpcommon.pubsub.consumer.MessageSubscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

class NotifierMessageProcessorTest {

	@Mock
	private NotificationRouter notificationRouter;

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private NotifierMessageProcessor notifierMessageProcessor;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(notifierMessageProcessor, "notifierSubProject", "test-project");
		ReflectionTestUtils.setField(notifierMessageProcessor, "notifierSubName", "test-sub");
		ReflectionTestUtils.setField(notifierMessageProcessor, "notifierSubMaxMessages", 10);
	}

	@Test
	void testReceiveNotifierMessages() {
		MessageSubscriber subscriber = notifierMessageProcessor.receiveNotifierMessages();
		assertNotNull(subscriber);
	}

	@Test
	void testProcess() throws Exception {
		BKNotificationMessage message = new BKNotificationMessage();
		PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
				.setData(ByteString.copyFromUtf8("{\"domainType\":\"test\"}")).build();

		when(objectMapper.readValue(any(String.class), eq(BKNotificationMessage.class))).thenReturn(message);

		notifierMessageProcessor.process(pubsubMessage);

		verify(notificationRouter, times(1)).route(any(BKNotificationMessage.class));
	}
}
