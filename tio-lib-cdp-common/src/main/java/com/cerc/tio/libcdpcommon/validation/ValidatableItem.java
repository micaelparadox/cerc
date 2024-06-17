package com.cerc.tio.libcdpcommon.validation;

import jakarta.validation.Validator;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Getter
public class ValidatableItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected transient ArrayList<String> errors = new ArrayList<>();

    /**
     * This method validates the current object using the provided {@link Validator}.
     * It populates the errors list with any validation errors found.
     *
     * @param validator The Validator to use for validation.
     */
    public void validate(final Validator validator) {
        errors = validator.validate(this).stream()
                .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * This method validates a child ValidatableItem using the provided {@link Validator}.
     * It calls {@link ValidatableItem#validate(Validator)} on the child item.
     * and adds any validation errors found to the current object's errors list.
     *
     * @param validator The Validator to use for validation.
     * @param validatableItem The child ValidatableItem to validate.
     */
    public final void validateChild(final Validator validator, ValidatableItem validatableItem) {
        ofNullable(validatableItem).ifPresent(item -> {
            item.validate(validator);
            this.errors.addAll(item.getErrors());
        });
    }

    /**
     * This method validates multiple children ValidatableItems using the provided {@link Validator}.
     * It calls {@link ValidatableItem#validate(Validator)} on each child item.
     * It adds any validation errors found to the current object's errors list.
     *
     * @param validator The Validator to use for validation.
     * @param validatableItems The children ValidatableItems to validate.
     */
    @SafeVarargs
    public final void validateChildren(final Validator validator, List<? extends ValidatableItem>... validatableItems) {
        ofNullable(validatableItems).ifPresent(itemsLists -> {
            for (List<? extends ValidatableItem> items : itemsLists) {
                ofNullable(items).ifPresent(list ->
                    list.forEach(item -> validateChild(validator, item))
                );
            }
        });
    }

    /**
     * This method checks if the current object is valid.
     * It is considered valid if the errors list is empty.
     *
     * @return A boolean indicating if the current object is valid.
     */
    public boolean isValid() {
        return errors.isEmpty();
    }
}
