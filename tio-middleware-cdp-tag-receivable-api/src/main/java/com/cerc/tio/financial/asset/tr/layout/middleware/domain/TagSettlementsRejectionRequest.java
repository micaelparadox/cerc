package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.cerc.tio.libcdpcommon.validation.ValidatableItem;
import jakarta.validation.Validator;
import lombok.*;

import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagSettlementsRejectionRequest extends ValidatableItem {
    private String idempotencyKey;
    private List<TagSettlementRejection> settlements;

    @Override
    public void validate(Validator validator) {
        super.validateChildren(validator, settlements);
    }
}