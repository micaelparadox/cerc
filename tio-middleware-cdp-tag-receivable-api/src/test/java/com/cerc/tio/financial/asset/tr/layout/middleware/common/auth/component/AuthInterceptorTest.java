package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.component;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthInterceptorTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthInterceptor underTest;

    @Test
    void doFilterInternal_Should_filter_request_When_authorized() throws IOException, ServletException {
        // given
        var request = mock(HttpServletRequest.class);
        given(request.getHeader("company-document")).willReturn("document");
        given(request.getServletPath()).willReturn("/estabelecimento");
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(request, response);

        // when
        when(authService.autorizeArPs(any())).thenReturn(Pair.of(true, "Authorized"));
        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(authService).autorizeArPs(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_Should_return_unauthorized_When_not_authorized() throws IOException, ServletException {
        // given
        var request = mock(HttpServletRequest.class);
        given(request.getHeader("company-document")).willReturn("document");
        given(request.getServletPath()).willReturn("/estabelecimento");
        var response = mock(HttpServletResponse.class);
        doNothing().when(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        var filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(request, response);

        // when
        when(authService.autorizeArPs(any())).thenReturn(Pair.of(false, "Unauthorized"));
        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(authService).autorizeArPs(any());
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_Should_filter_When_route_isnt_productRoute() throws IOException, ServletException {
        // given
        var request = mock(HttpServletRequest.class);
        given(request.getHeader("company-document")).willReturn("document");
        given(request.getServletPath()).willReturn("/route");
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(request, response);

        // when
        underTest.doFilterInternal(request, response, filterChain);

        // then
        verify(authService, never()).autorizeArPs(any());
        verify(filterChain).doFilter(request, response);
    }
}