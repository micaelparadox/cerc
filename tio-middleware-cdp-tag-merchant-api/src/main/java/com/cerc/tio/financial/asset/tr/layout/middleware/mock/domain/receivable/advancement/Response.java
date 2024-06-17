package com.cerc.tio.financial.asset.tr.layout.middleware.mock.domain.receivable.advancement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
        private List<AdvancementsResponse> advancements = null;
        private String processKey = null;
        private String createdAt = null;

        public Response addAdvancementsItem(AdvancementsResponse advancementsItem) {
            if (this.advancements == null) {
                this.advancements = new ArrayList<>();
            }
            this.advancements.add(advancementsItem);
            return this;
        }

}
