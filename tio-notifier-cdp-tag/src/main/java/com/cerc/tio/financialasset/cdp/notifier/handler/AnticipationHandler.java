package com.cerc.tio.financialasset.cdp.notifier.handler;

import com.cerc.tio.financialasset.cdp.notifier.config.anotation.DomainType;
import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@DomainType("antecipation")
public class AnticipationHandler implements MessageHandler{

        @Override
        public void handle(BKNotificationMessage message) {
            log.info("Handling Antecipation: {}", message);
        }
}
