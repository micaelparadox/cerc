package com.cerc.tio.financial.asset.tr.layout.middleware.domain.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseItem {
    private String key;
    private String reference;

}