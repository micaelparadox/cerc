package com.cerc.tio.libcdpcommon.domain.bookkeeping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BKNotificationConfig {
    private String eventName;
    private String eventUrl;
}
