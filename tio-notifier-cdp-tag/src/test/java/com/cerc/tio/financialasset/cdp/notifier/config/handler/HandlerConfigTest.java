package com.cerc.tio.financialasset.cdp.notifier.config.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import com.cerc.tio.financialasset.cdp.notifier.config.anotation.DomainType;
import com.cerc.tio.financialasset.cdp.notifier.handler.MessageHandler;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationMessage;

class HandlerConfigTest {

	private ApplicationContext context;
	private HandlerConfig handlerConfig;

	@BeforeEach
	void setUp() {
		context = mock(ApplicationContext.class);
		handlerConfig = new HandlerConfig();
	}

	@DomainType("type1")
	static class Handler1 implements MessageHandler {
		@Override
		public void handle(BKNotificationMessage message) {
		}
	}

	@DomainType("type2")
	static class Handler2 implements MessageHandler {
		@Override
		public void handle(BKNotificationMessage message) {
		}
	}

	@Test
	void testDomainHandlers() {

		MessageHandler handler1 = new Handler1();
		MessageHandler handler2 = new Handler2();

		Map<String, MessageHandler> beansOfType = new HashMap<>();
		beansOfType.put("handler1", handler1);
		beansOfType.put("handler2", handler2);

		when(context.getBeansOfType(MessageHandler.class)).thenReturn(beansOfType);

		Map<String, MessageHandler> domainHandlers = handlerConfig.domainHandlers(context);

		assertNotNull(domainHandlers);
		assertEquals(2, domainHandlers.size());
		assertTrue(domainHandlers.containsKey("type1"));
		assertTrue(domainHandlers.containsKey("type2"));
	}
}
