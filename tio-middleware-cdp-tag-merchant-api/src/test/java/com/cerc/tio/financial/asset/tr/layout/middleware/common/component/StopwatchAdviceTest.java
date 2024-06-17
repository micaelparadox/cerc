package com.cerc.tio.financial.asset.tr.layout.middleware.common.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@TestInstance(Lifecycle.PER_CLASS)
public class StopwatchAdviceTest {

    private ProceedingJoinPoint mockProceedingJoinPoint;
    private Stopwatch mockStopwatch;
    private Signature mockSignature;
    private Fool fool;

    @BeforeAll
    public void init() throws Throwable {
        mockProceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        mockStopwatch = Mockito.mock(Stopwatch.class);
        mockSignature = Mockito.mock(Signature.class);

        when(mockStopwatch.prefix()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "Prefix";
            }
        });

        fool = new Fool();
        when(mockProceedingJoinPoint.getTarget()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return fool;
            }
        });

        when(mockProceedingJoinPoint.proceed()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return fool;
            }
        });

        when(mockSignature.getName()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "Name";
            }
        });

        when(mockProceedingJoinPoint.getSignature()).thenAnswer(new Answer<Signature>() {
            @Override
            public Signature answer(InvocationOnMock invocation) throws Throwable {
                return mockSignature;
            }
        });

    }

    @Test
    void testAdvice() throws Throwable {
        var target = new StopwatchAdvice();
        var result = target.advice(mockProceedingJoinPoint, mockStopwatch);
        assertEquals(fool, result);
    }

    @Test
    void testGetStopWatchTag() throws Throwable {
        var target = new StopwatchAdvice();
        var result = target.getStopWatchTag(mockStopwatch, mockProceedingJoinPoint);
        System.err.println(result);
        assertEquals(result, "Prefix.Fool.Name");
    }

    @Test
    void testIsSuccessRequest() {
        var target = new StopwatchAdvice();
        var result = target.isSuccessRequest(null);
        assertTrue(result);
    }

    @Test
    void testIsInsuccessRequest() {
        var target = new StopwatchAdvice();
        var result = target.isSuccessRequest(new Exception());
        assertFalse(result);
    }

    @Test
    void testIsProceedThrowsException() throws Throwable {
        when(mockProceedingJoinPoint.proceed()).thenThrow(new IllegalAccessError());
        var target = new StopwatchAdvice();
        assertThrows(IllegalAccessError.class, ()->target.advice(mockProceedingJoinPoint, mockStopwatch));
    }

    public static class Fool {

        @Override
        public String toString() {
            return "Fool test";
        }
    }

}