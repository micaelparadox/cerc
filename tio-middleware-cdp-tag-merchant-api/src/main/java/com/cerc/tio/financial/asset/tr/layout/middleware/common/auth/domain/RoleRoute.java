package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum RoleRoute {
    AP001("AP001", "/customer/commercialEstablishment");

    private final String productCode;
    private final String route;

    public static String getProductByRoute(String route) {
        return Stream.of(RoleRoute.values())
                .filter(roleRoute -> roleRoute.route.equals(route))
                .map(roleRoute -> roleRoute.productCode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Route " + route + " not found"));
    }
}
