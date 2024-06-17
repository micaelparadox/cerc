package com.cerc.tio.libcdpcommon.util.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProcessingIds {
    private final String companyDocument;
    private final String companyId;
    private final String correlationId;
}
