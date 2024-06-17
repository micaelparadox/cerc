package com.cerc.tio.financial.asset.tr.layout.middleware.domain;

import com.cerc.tio.financial.asset.tr.layout.middleware.domain.common.TagResponseItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagAdvancementsResponse {
    private String processKey;
    private String createdAt;
    private List<TagResponseItem> advancements;
    private List<String> errors;

}