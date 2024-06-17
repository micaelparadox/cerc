package com.cerc.tio.financialasset.cdp.notifier.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationMessage;

class AnticipationHandlerTest {

	private AnticipationHandler anticipationHandler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		anticipationHandler = new AnticipationHandler();
	}

	@Test
	void testHandle() {
		BKNotificationMessage message = new BKNotificationMessage();
		anticipationHandler.handle(message);
		// WIP: adicionar outras assertivas
	}
}
