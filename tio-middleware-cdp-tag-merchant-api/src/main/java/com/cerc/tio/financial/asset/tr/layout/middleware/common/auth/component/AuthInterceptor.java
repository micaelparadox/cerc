package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.component;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.AuthRequest;
import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.RoleRoute;
import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@DependsOn("authService")
@RequiredArgsConstructor
public class AuthInterceptor extends OncePerRequestFilter {

    private final AuthService authService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain
    ) throws IOException, ServletException {
        final String companyDocument = request.getHeader("company-document");
        final String path = request.getServletPath();

        final String processCode;
        try {
            processCode = RoleRoute.getProductByRoute(path);
        }
        catch (IllegalArgumentException e) {
            LOGGER.debug("Route not found: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        final AuthRequest authRequest = buildAuthRequest(companyDocument, processCode);

        final Pair<Boolean, String> authorizeResponse = authService.authorizeParticipant(authRequest);
        final String authorizeResponseMessage = authorizeResponse.getRight();
        final boolean isAuthorized = authorizeResponse.getLeft();

        if (!isAuthorized) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authorizeResponseMessage);
        }
        filterChain.doFilter(request, response);
    }

    private AuthRequest buildAuthRequest(String companyDocument, String processCode) {
        return AuthRequest.builder()
            .arDocument(companyDocument)
            .psDocument(companyDocument)
            .processCode(processCode)
            .walletCode(null)
            .build();
    }

}
