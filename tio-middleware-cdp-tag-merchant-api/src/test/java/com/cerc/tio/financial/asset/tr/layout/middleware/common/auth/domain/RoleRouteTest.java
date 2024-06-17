package com.cerc.tio.financial.asset.tr.layout.middleware.common.auth.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleRouteTest {

    @ParameterizedTest
    @EnumSource(RoleRoute.class)
    void getProductByRoute_Should_return_productCode_When_parameter_is_valid(RoleRoute roleRoute) {
        // Arrange
        String route = roleRoute.getRoute();
        // Act
        String productCode = RoleRoute.getProductByRoute(route);
        // Assert
        assertEquals(roleRoute.getProductCode(), productCode);
    }

    @Test
    void getProductByRoute_Should_throw_IllegalArgumentException_When_parameter_is_invalid() {
        // Arrange
        String route = "/invalid";
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> RoleRoute.getProductByRoute(route));
    }


}