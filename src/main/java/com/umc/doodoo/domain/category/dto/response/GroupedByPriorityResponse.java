package com.umc.doodoo.domain.category.dto.response;

import java.time.LocalDate;
import java.util.List;

public record GroupedByPriorityResponse(
        LocalDate date,
        List<PriorityGroupResponse> priorities
) {
}
