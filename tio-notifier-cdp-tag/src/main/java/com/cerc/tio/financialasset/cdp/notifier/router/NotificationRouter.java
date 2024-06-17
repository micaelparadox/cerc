package com.cerc.tio.financialasset.cdp.notifier.router;

import com.cerc.tio.financialasset.cdp.notifier.handler.MessageHandler;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRouter {

    @Autowired
    @Qualifier("domainHandlers")
    private  Map<String, MessageHandler> domainHandlers;

    public void route(BKNotificationMessage message) {
        String domainType = message.getDomainType();

        MessageHandler handler = domainHandlers.get(domainType);
        if (handler == null) {
            log.error("No handler found for domain type: {}", domainType);
        }
        handler.handle(message);
    }
}
