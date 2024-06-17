package com.cerc.tio.financialasset.cdp.notifier.router;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cerc.tio.financialasset.cdp.notifier.handler.MessageHandler;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationMessage;

class NotificationRouterTest {

	@Mock
	private Map<String, MessageHandler> domainHandlers;

	@InjectMocks
	private NotificationRouter notificationRouter;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRoute() throws NoSuchFieldException, IllegalAccessException {
		BKNotificationMessage message = new BKNotificationMessage();

		Field domainTypeField = BKNotificationMessage.class.getDeclaredField("domainType");
		domainTypeField.setAccessible(true);
		domainTypeField.set(message, "test");

		MessageHandler handler = mock(MessageHandler.class);
		when(domainHandlers.get("test")).thenReturn(handler);

		notificationRouter.route(message);

		verify(handler, times(1)).handle(message);
	}
}
