package com.cerc.tio.financialasset.cdp.notifier.handler;

import com.cerc.tio.libcdpcommon.domain.bookkeeping.BKNotificationMessage;

public interface MessageHandler {

    void handle(BKNotificationMessage message);

}
