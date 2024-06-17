package com.cerc.tio.financial.asset.tr.layout.middleware.common.config;

import com.cerc.tio.libcdpcommon.pubsub.publisher.BookkeepingPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PublisherConfig {

    @Value("${pubsub.bookkeeping.project-id}")
    private String bookkeepingProjectId;

    @Bean
    public BookkeepingPublisher bookkeepingPublisher() {
        return new BookkeepingPublisher(bookkeepingProjectId);
    }
}
