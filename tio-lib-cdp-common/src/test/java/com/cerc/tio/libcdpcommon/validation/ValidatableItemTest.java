package com.cerc.tio.libcdpcommon.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatableItemTest {

    Validator validator;

    @BeforeEach
    void setUp() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validate_Should_return_empty_set_When_item_is_valid() {
        // Given
        TestValidatableItem item = TestValidatableItem.createFrom("name");

        // When
        item.validate(validator);

        // Then
        assertTrue(item.getErrors().isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { " ", "" })
    void validate_Should_return_violations_When_item_is_invalid(String name) {
        // Given
        TestValidatableItem item = TestValidatableItem.createFrom(name);

        // When
        item.validate(validator);

        // Then
        assertFalse(item.getErrors().isEmpty());
    }

    @Test
    void isValid_Should_return_true_When_item_is_valid() {
        // Given
        TestValidatableItem item = TestValidatableItem.createFrom("name");

        // When
        item.validate(validator);

        // Then
        assertTrue(item.isValid());
    }

    @RequiredArgsConstructor
    public static class TestValidatableItem extends ValidatableItem {
        private final List<ChildItem> childItems;
        @RequiredArgsConstructor
        public static class ChildItem extends ValidatableItem {
            @NotBlank
            @SuppressWarnings("unused")
            private final String name;
        }

        @Override
        public void validate(Validator validator) {
            super.validate(validator);
            validateChildren(validator, childItems);
        }

        private static TestValidatableItem createFrom(String name) {
            return new TestValidatableItem(List.of(new ChildItem(name)));
        }
    }

}