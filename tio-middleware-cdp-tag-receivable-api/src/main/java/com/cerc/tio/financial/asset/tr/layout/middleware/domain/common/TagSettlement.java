package com.cerc.tio.financial.asset.tr.layout.middleware.domain.common;

import static com.cerc.tio.libcdpcommon.util.UUIDv5Util.generateUUIDv5;
import static com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType.CNPJ;
import static com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType.CPF;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKTransactionDomainType;
import com.cerc.tio.libcdpcommon.validation.ValidatableItem;
import com.cerc.tio.libcdpcommon.validation.annotation.DatePattern;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocument;
import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocumentType;

import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagSettlement extends ValidatableItem {
	@NotNull(message = "reference is required")
	private String reference;

	@NotNull(message = "asset holder document type is required")
	@ValidateDocumentType(type = { CNPJ, CPF }, message = "invalid document type")
	private String assetHolderDocumentType;

	@NotNull(message = "asset holder is required")
	@ValidateDocument(type = { CNPJ, CPF }, message = "invalid document type")
	private String assetHolder;

	@NotNull(message = "original asset holder document is required")
	@ValidateDocument(type = { CNPJ, CPF }, message = "invalid document type")
	private String originalAssetHolder;

	@NotNull(message = "settlement date is required")
	@DatePattern(message = "invalid settlement date")
	private String settlementDate;

	@NotNull(message = "settlement obligation date is required")
	@DatePattern(message = "invalid settlement obligation date")
	private String settlementObligationDate;

	@NotNull(message = "payment scheme is required")
	@Size(min = 3, max = 3, message = "invalid payment scheme code length")
	private String paymentScheme;

	@NotNull(message = "amount is required")
	@Min(value = 0, message = "amount must be greater than or equal to 0")
	private BigInteger amount;

	private TagBankAccount bankAccount;

	public String getFinancialAssetCommitmentHash(String payer) {
		return String.format("%s_%s_%s_%s_%s", originalAssetHolder, originalAssetHolder, payer, paymentScheme,
				settlementObligationDate);
	}

	public String getFinancialAssetSettlementKey(String payer) {
		// TODO: Define namespace in a more meaningful way
		String namespace = UUID.nameUUIDFromBytes(BKTransactionDomainType.FINANCIAL_ASSET.getDomain().getBytes())
				.toString();
		String commitmentHash = getFinancialAssetCommitmentHash(payer);
		String financialAssetSettlementKey = null;

		try {
			financialAssetSettlementKey = generateUUIDv5(namespace, commitmentHash).toString();
		} catch (NoSuchAlgorithmException e) {
			log.error("Error generating receivable key", e);
		}

		return financialAssetSettlementKey;
	}

	@Override
	public void validate(Validator validator) {
		super.validate(validator);
		super.validateChild(validator, bankAccount);
	}

}
