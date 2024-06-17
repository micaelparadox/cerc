package com.cerc.tio.libcdpcommon.pubsub.publisher.exception;

public class PublisherCreationException extends RuntimeException {
    public PublisherCreationException(String message) {
        super(message);
    }

    public PublisherCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}