package com.umc.doodoo.domain.category.dto.response;

import java.util.List;

public record CategoryListResponse(
        int totalCount,
        List<CategoryListItemResponse> categories
) {
}
