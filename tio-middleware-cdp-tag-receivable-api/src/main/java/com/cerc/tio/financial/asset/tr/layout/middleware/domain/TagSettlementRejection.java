package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.cerc.tio.libcdpcommon.validation.ValidatableItem;

import jakarta.validation.constraints.NotNull;
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
public class TagSettlementRejection extends ValidatableItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Key cannot be null")
	private String key;

	@NotNull(message = "Reason details cannot be null")
	private String reasonDetails;
}
