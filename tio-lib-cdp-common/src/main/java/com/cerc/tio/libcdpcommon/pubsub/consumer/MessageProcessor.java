package com.cerc.tio.libcdpcommon.pubsub.consumer;

import com.google.pubsub.v1.PubsubMessage;

public interface MessageProcessor {
    void process(PubsubMessage pubsubMessage);
}