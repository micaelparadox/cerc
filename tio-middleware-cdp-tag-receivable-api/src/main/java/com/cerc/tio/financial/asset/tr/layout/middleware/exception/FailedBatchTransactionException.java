package com.cerc.tio.financial.asset.tr.layout.middleware.exception;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.TagErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FailedBatchTransactionException extends RuntimeException {
    private final transient TagErrorResponse tagErrorResponse;
}
