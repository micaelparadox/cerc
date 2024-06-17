package com.cerc.tio.financialasset.cdp.notifier.config.handler;

import com.cerc.tio.financialasset.cdp.notifier.config.anotation.DomainType;
import com.cerc.tio.financialasset.cdp.notifier.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class HandlerConfig {

    @Bean(name = "domainHandlers")
    public Map<String, MessageHandler> domainHandlers(ApplicationContext context) {
        Map<String, MessageHandler> domainHandlers = new HashMap<>();
        context.getBeansOfType(MessageHandler.class).forEach((key, value) -> {
            DomainType domainTypeAnnotation = value.getClass().getAnnotation(DomainType.class);
            if (domainTypeAnnotation != null) {
                String domainType = domainTypeAnnotation.value();
                log.info("Registering handler for domainType: {} -> {}", domainType, value.getClass().getSimpleName());
                domainHandlers.put(domainType, value);
            } else {
                log.warn("No DomainType annotation found for handler: {}", key);
            }
        });
        return domainHandlers;
    }


}
