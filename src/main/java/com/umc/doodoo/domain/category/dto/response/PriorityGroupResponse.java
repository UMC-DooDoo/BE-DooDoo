package com.umc.doodoo.domain.category.dto.response;

import java.util.List;

public record PriorityGroupResponse(
        Integer priority,
        List<TodoInPriorityResponse> todos
) {
}
