package com.cerc.tio.financial.asset.tr.layout.middleware.common.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StopwatchAdviceTest {

	private StopwatchAdvice stopwatchAdvice;
	private ProceedingJoinPoint joinPoint;
	private Stopwatch stopwatch;
	private Signature signature;

	@BeforeEach
	void setUp() {
		stopwatchAdvice = new StopwatchAdvice();
		joinPoint = mock(ProceedingJoinPoint.class);
		stopwatch = mock(Stopwatch.class);
		signature = mock(Signature.class);

		when(joinPoint.getTarget()).thenReturn(this);
		when(joinPoint.getSignature()).thenReturn(signature);
		when(signature.getName()).thenReturn("testMethod");
	}

	@Test
	void testAdviceSuccess() throws Throwable {
		when(joinPoint.proceed()).thenReturn("Success");

		Object result = stopwatchAdvice.advice(joinPoint, stopwatch);

		assertEquals("Success", result);
		verify(joinPoint, times(1)).proceed();
	}

	@Test
	void testAdviceWithException() throws Throwable {
		when(joinPoint.proceed()).thenThrow(new RuntimeException("Test Exception"));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			stopwatchAdvice.advice(joinPoint, stopwatch);
		});

		assertEquals("Test Exception", exception.getMessage());
		verify(joinPoint, times(1)).proceed();
	}

	@Test
	void testGetStopWatchTag() {
		String tag = stopwatchAdvice.getStopWatchTag(stopwatch, joinPoint);

		assertNotNull(tag);
		assertTrue(tag.contains("StopwatchAdviceTest"));
		assertTrue(tag.contains("testMethod"));
	}

	@Test
	void testIsSuccessRequest() {
		assertTrue(stopwatchAdvice.isSuccessRequest(null));
		assertFalse(stopwatchAdvice.isSuccessRequest(new RuntimeException("Test Exception")));
	}
}
