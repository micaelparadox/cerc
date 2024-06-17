package com.cerc.tio.financialasset.cdp.notifier.handler;

import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

class FinancialAssetHandlerTest {

	private FinancialAssetHandler financialAssetHandler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		financialAssetHandler = new FinancialAssetHandler();
	}

	@Test
	void testHandle() {
		BKNotificationMessage message = new BKNotificationMessage();
		financialAssetHandler.handle(message);
		// WIP: adicionar outras assertivas
	}
}
