package com.uom.lims.api.supermanagement.dto.response;

import com.uom.lims.api.dto.response.OrdersBillingStatsResponse;
import com.uom.lims.api.dto.response.PhlebotomyStatsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuperReportsResponse {
    private OrdersBillingStatsResponse ordersBilling;
    private PhlebotomyStatsResponse phlebotomy;
}

