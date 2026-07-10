package com.umc.doodoo.domain.category.controller;

import com.umc.doodoo.domain.category.dto.request.CategoryCreateRequest;
import com.umc.doodoo.domain.category.dto.request.CategoryUpdateRequest;
import com.umc.doodoo.domain.category.dto.response.CategoryCreateResponse;
import com.umc.doodoo.domain.category.dto.response.CategoryListResponse;
import com.umc.doodoo.domain.category.dto.response.CategoryUpdateResponse;
import com.umc.doodoo.domain.category.dto.response.GroupedByCategoryResponse;
import com.umc.doodoo.domain.category.dto.response.GroupedByPriorityResponse;
import com.umc.doodoo.domain.category.service.CategoryService;
import com.umc.doodoo.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "분야 관리 API")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "분야 생성", description = "새로운 분야를 생성합니다.")
    @PostMapping
    public ApiResponse<CategoryCreateResponse> createCategory(@RequestBody @Valid CategoryCreateRequest request) {
        return ApiResponse.onSuccess(categoryService.createCategory(request));
    }

    @Operation(summary = "분야 목록 조회", description = "특정 멤버의 분야 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<CategoryListResponse> getCategories(
            @RequestParam Long memberId
    ) {
        return ApiResponse.onSuccess(categoryService.getCategories(memberId));
    }

    @Operation(summary = "분야 수정", description = "분야의 이름 또는 색상을 부분 수정합니다.")
    @PatchMapping("/{categoryId}")
    public ApiResponse<CategoryUpdateResponse> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryUpdateRequest request
    ) {
        return ApiResponse.onSuccess(categoryService.updateCategory(categoryId, request));
    }

    @Operation(summary = "분야 삭제", description = "분야를 삭제합니다.")
    @DeleteMapping("/{categoryId}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.onSuccess("분야가 삭제되었습니다.", null);
    }

    @Operation(summary = "분야별 할일 그룹 조회", description = "특정 날짜의 할일을 분야별로 그룹핑하여 조회합니다.")
    @GetMapping("/todos/grouped-by-category")
    public ApiResponse<GroupedByCategoryResponse> getGroupedByCategory(
            @RequestParam Long memberId,
            @RequestParam @NotBlank String date
    ) {
        return ApiResponse.onSuccess(categoryService.getGroupedByCategory(memberId, date));
    }

    @Operation(summary = "우선순위별 할일 그룹 조회", description = "특정 날짜의 할일을 우선순위별로 그룹핑하여 조회합니다.")
    @GetMapping("/todos/grouped-by-priority")
    public ApiResponse<GroupedByPriorityResponse> getGroupedByPriority(
            @RequestParam Long memberId,
            @RequestParam @NotBlank String date
    ) {
        return ApiResponse.onSuccess(categoryService.getGroupedByPriority(memberId, date));
    }
}
