package com.cerc.tio.financial.asset.tr.layout.middleware.common.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

class CorsConfigTest {

	private CorsConfig corsConfig;
	private CorsRegistry corsRegistry;
	private CorsRegistration corsRegistration;

	@BeforeEach
	void setUp() {
		corsConfig = new CorsConfig();
		corsRegistry = mock(CorsRegistry.class);
		corsRegistration = mock(CorsRegistration.class);

		when(corsRegistry.addMapping("/**")).thenReturn(corsRegistration);
		when(corsRegistration.allowedOrigins("*")).thenReturn(corsRegistration);
		when(corsRegistration.allowedMethods("*")).thenReturn(corsRegistration);
		when(corsRegistration.allowedHeaders("*")).thenReturn(corsRegistration);
	}

	@Test
	void testAddCorsMappings() {
		corsConfig.addCorsMappings(corsRegistry);

		verify(corsRegistry).addMapping("/**");
		verify(corsRegistration).allowedOrigins("*");
		verify(corsRegistration).allowedMethods("*");
		verify(corsRegistration).allowedHeaders("*");
	}
}
