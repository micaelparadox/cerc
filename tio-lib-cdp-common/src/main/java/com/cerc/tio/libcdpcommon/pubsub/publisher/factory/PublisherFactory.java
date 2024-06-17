package com.cerc.tio.libcdpcommon.pubsub.publisher.factory;

import com.google.api.gax.retrying.RetrySettings;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.TopicName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.threeten.bp.Duration;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublisherFactory {

    public static Publisher createPublisher(String projectId, String topicId) throws IOException {
        TopicName topicName = TopicName.of(projectId, topicId);
        RetrySettings retrySettings = RetrySettings.newBuilder()
            .setInitialRetryDelay(Duration.ofMillis(100))
            .setRetryDelayMultiplier(2.0)
            .setMaxRetryDelay(Duration.ofSeconds(60))
            .setInitialRpcTimeout(Duration.ofSeconds(5))
            .setRpcTimeoutMultiplier(1.0)
            .setMaxRpcTimeout(Duration.ofSeconds(5))
            .setTotalTimeout(Duration.ofSeconds(120))
            .build();

        return Publisher
            .newBuilder(topicName)
            .setRetrySettings(retrySettings)
            .build();
    }
}
