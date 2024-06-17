package com.cerc.tio.libcdpcommon.exception;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.cerc.tio.libcdpcommon.domain.tag.TagErrorResponse;

import lombok.Getter;

/**
 * This class represents a custom exception that is thrown when a batch
 * transaction fails. It extends {@link RuntimeException}, meaning it is an
 * unchecked exception. This is meant to be used with Spring's ControllerAdvice
 * to handle the exception globally.
 */
@Getter
public class FailedBatchTransactionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final DateTimeFormatter ZONED_DATE_TIME_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

	private final transient TagErrorResponse tagErrorResponse;

	public FailedBatchTransactionException(TagErrorResponse tagError) {
		super(tagError.toString());
		this.tagErrorResponse = tagError;
	}

	public static FailedBatchTransactionException of(List<String> errors, String correlationId) {
		var tagError = TagErrorResponse.builder().processKey(correlationId)
				.createdAt(ZonedDateTime.now().format(ZONED_DATE_TIME_FORMATTER)).errors(errors).build();

		return new FailedBatchTransactionException(tagError);
	}

	public List<String> getErrors() {
		return tagErrorResponse.getErrors();
	}
}
