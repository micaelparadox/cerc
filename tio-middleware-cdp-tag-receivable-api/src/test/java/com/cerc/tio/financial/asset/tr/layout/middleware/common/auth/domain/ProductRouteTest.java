package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain;

import com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain.ProductRoute;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductRouteTest {

    @ParameterizedTest
    @EnumSource(ProductRoute.class)
    void getProductByRoute_Should_return_productCode_When_parameter_is_valid(ProductRoute productRoute) {
        // Arrange
        String route = productRoute.getRoute();
        // Act
        String productCode = ProductRoute.getProductByRoute(route);
        // Assert
        assertEquals(productRoute.getProductCode(), productCode);
    }

    @Test
    void getProductByRoute_Should_throw_IllegalArgumentException_When_parameter_is_invalid() {
        // Arrange
        String route = "/invalid";
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ProductRoute.getProductByRoute(route));
    }


}