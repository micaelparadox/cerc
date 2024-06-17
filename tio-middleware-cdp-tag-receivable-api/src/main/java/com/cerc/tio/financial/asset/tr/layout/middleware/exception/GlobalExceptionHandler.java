package com.cerc.tio.financial.asset.tr.layout.middleware.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cerc.tio.libcdpcommon.domain.tag.TagErrorResponse;
import com.cerc.tio.libcdpcommon.exception.FailedBatchTransactionException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(FailedBatchTransactionException.class)
	public ResponseEntity<TagErrorResponse> handleFailedBatchTransactionException(FailedBatchTransactionException ex) {
		return ResponseEntity.badRequest().body(ex.getTagErrorResponse());
	}
}