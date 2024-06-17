package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum ProductRoute {
    AP001("AP001", "/estabelecimento");

    private final String productCode;
    private final String route;

    public static String getProductByRoute(String route) {
        return Stream.of(ProductRoute.values())
                .filter(productRoute -> productRoute.route.equals(route))
                .map(productRoute -> productRoute.productCode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Route " + route + " not found"));
    }
}
