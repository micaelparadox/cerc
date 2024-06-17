package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import java.util.List;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagSettlement;
import com.cerc.tio.libcdpcommon.validation.ValidatableItem;

import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagSettlementsRequest extends ValidatableItem {
	private static final long serialVersionUID = 1L;
    private String idempotencyKey;
    private List<TagSettlement> settlements;

    @Override
    public void validate(Validator validator) {
    	super.validate(validator);
    	super.validateChildren(validator, settlements);
    }
    

}