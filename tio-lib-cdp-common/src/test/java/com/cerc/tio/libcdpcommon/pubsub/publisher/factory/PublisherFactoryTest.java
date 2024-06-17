package com.cerc.tio.libcdpcommon.pubsub.publisher.factory;

import com.google.cloud.pubsub.v1.Publisher;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.cerc.tio.libcdpcommon.pubsub.publisher.factory.PublisherFactory.createPublisher;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PublisherFactoryTest {

    @Test
    void createPublisher_shouldReturnPublisher_whenProjectIdAndTopicIdAreValid() throws IOException {
        Publisher underTest = createPublisher("projectId", "topicId");

        assertNotNull(underTest);
        assertEquals("projects/projectId/topics/topicId", underTest.getTopicNameString());
    }
}