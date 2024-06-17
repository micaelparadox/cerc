package com.cerc.tio.libcdpcommon.pubsub.publisher.extension;

import com.google.api.core.ApiFutureCallback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiFuturesCalbackImpl implements ApiFutureCallback<String> {
    private final String trace;

    public ApiFuturesCalbackImpl(String trace) {
        this.trace = trace;
    }

    @Override
    public void onFailure(Throwable t) {
        log.error("[{}] Failed to publish message!", trace, t);
    }

    @Override
    public void onSuccess(String result) {
        log.debug("[{}] Message published successfully with id [{}]!", trace, result);
    }
}
