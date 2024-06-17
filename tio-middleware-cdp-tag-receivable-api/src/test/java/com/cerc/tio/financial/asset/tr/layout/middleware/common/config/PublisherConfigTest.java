package com.cerc.tio.financial.asset.tr.layout.middleware.common.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.cerc.tio.libcdpcommon.pubsub.publisher.BookkeepingPublisher;

class PublisherConfigTest {

	@InjectMocks
	private PublisherConfig publisherConfig;

	@Mock
	private BookkeepingPublisher bookkeepingPublisher;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(publisherConfig, "bookkeepingProjectId", "test-project-id");
	}

	@Test
	void testBookkeepingPublisher() {
		BookkeepingPublisher publisher = publisherConfig.bookkeepingPublisher();
		assertNotNull(publisher);
	}
}
