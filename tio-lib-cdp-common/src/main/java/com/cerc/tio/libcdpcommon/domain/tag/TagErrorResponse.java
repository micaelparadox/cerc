package com.cerc.tio.libcdpcommon.domain.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagErrorResponse {
    private String processKey;
    private String createdAt;
    private List<String> errors;
}
