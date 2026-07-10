package com.umc.doodoo.domain.category.service;

import com.umc.doodoo.domain.category.dto.request.CategoryCreateRequest;
import com.umc.doodoo.domain.category.dto.request.CategoryUpdateRequest;
import com.umc.doodoo.domain.category.dto.response.CategoryCreateResponse;
import com.umc.doodoo.domain.category.dto.response.CategoryListItemResponse;
import com.umc.doodoo.domain.category.dto.response.CategoryListResponse;
import com.umc.doodoo.domain.category.dto.response.CategoryUpdateResponse;
import com.umc.doodoo.domain.category.dto.response.CategoryWithTodosResponse;
import com.umc.doodoo.domain.category.dto.response.GroupedByCategoryResponse;
import com.umc.doodoo.domain.category.dto.response.GroupedByPriorityResponse;
import com.umc.doodoo.domain.category.dto.response.PriorityGroupResponse;
import com.umc.doodoo.domain.category.dto.response.TodoInCategoryResponse;
import com.umc.doodoo.domain.category.dto.response.TodoInPriorityResponse;
import com.umc.doodoo.domain.category.entity.Category;
import com.umc.doodoo.domain.category.entity.Color;
import com.umc.doodoo.domain.category.exception.CategoryErrorCode;
import com.umc.doodoo.domain.category.repository.CategoryRepository;
import com.umc.doodoo.domain.todo.entity.Todo;
import com.umc.doodoo.domain.todo.repository.TodoRepository;
import com.umc.doodoo.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public CategoryCreateResponse createCategory(CategoryCreateRequest request) {
        if (request.memberId() == null || request.categoryName() == null
                || request.categoryName().isBlank() || request.categoryName().length() > 30
                || request.color() == null) {
            throw new CustomException(CategoryErrorCode.CATEGORY_INVALID_INPUT);
        }

        Color color = Color.fromValue(request.color());

        Category category = Category.builder()
                .memberId(request.memberId())
                .categoryName(request.categoryName())
                .color(color)
                .build();

        return CategoryCreateResponse.from(categoryRepository.save(category));
    }

    public CategoryListResponse getCategories(Long memberId) {
        if (memberId == null) {
            throw new CustomException(CategoryErrorCode.CATEGORY_INVALID_INPUT);
        }

        List<CategoryListItemResponse> items = categoryRepository.findByMemberId(memberId).stream()
                .map(CategoryListItemResponse::from)
                .toList();

        return new CategoryListResponse(memberId, items.size(), items);
    }

    @Transactional
    public CategoryUpdateResponse updateCategory(Long categoryId, CategoryUpdateRequest request) {
        Category category = findCategoryOrThrow(categoryId);

        if (request.categoryName() == null && request.color() == null) {
            throw new CustomException(CategoryErrorCode.CATEGORY_INVALID_INPUT);
        }

        if (request.categoryName() != null) {
            if (request.categoryName().isBlank() || request.categoryName().length() > 30) {
                throw new CustomException(CategoryErrorCode.CATEGORY_INVALID_INPUT);
            }
            category.updateCategoryName(request.categoryName());
        }
        if (request.color() != null) {
            category.updateColor(Color.fromValue(request.color()));
        }

        return CategoryUpdateResponse.from(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = findCategoryOrThrow(categoryId);
        categoryRepository.delete(category);
    }

    public GroupedByCategoryResponse getGroupedByCategory(Long memberId, String dateStr) {
        if (memberId == null || dateStr == null || dateStr.isBlank()) {
            throw new CustomException(CategoryErrorCode.CATEGORY_INVALID_INPUT);
        }

        LocalDate date = parseDate(dateStr);

        Map<Long, Category> categoryMap = categoryRepository.findByMemberId(memberId).stream()
                .collect(Collectors.toMap(Category::getId, c -> c));

        List<Todo> todos = todoRepository.findByUserIdAndTaskDate(memberId, date);

        Map<Long, List<Todo>> todosByCategoryId = todos.stream()
                .collect(Collectors.groupingBy(Todo::getCategoryId, LinkedHashMap::new, Collectors.toList()));

        List<CategoryWithTodosResponse> categories = todosByCategoryId.entrySet().stream()
                .filter(entry -> categoryMap.containsKey(entry.getKey()))
                .map(entry -> {
                    List<TodoInCategoryResponse> todoItems = entry.getValue().stream()
                            .map(TodoInCategoryResponse::from)
                            .toList();
                    return CategoryWithTodosResponse.from(categoryMap.get(entry.getKey()), todoItems);
                })
                .toList();

        return new GroupedByCategoryResponse(date, categories);
    }

    public GroupedByPriorityResponse getGroupedByPriority(Long memberId, String dateStr) {
        if (memberId == null || dateStr == null || dateStr.isBlank()) {
            throw new CustomException(CategoryErrorCode.CATEGORY_INVALID_INPUT);
        }

        LocalDate date = parseDate(dateStr);

        List<Todo> todos = todoRepository.findByUserIdAndTaskDate(memberId, date);

        List<Long> categoryIds = todos.stream()
                .map(Todo::getCategoryId)
                .distinct()
                .toList();

        Map<Long, Category> categoryMap = categoryRepository.findAllById(categoryIds).stream()
                .collect(Collectors.toMap(Category::getId, c -> c));

        List<PriorityGroupResponse> priorities = todos.stream()
                .collect(Collectors.groupingBy(
                        todo -> todo.getPriority().getValue(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    List<TodoInPriorityResponse> todoItems = entry.getValue().stream()
                            .map(todo -> TodoInPriorityResponse.from(todo, categoryMap.get(todo.getCategoryId())))
                            .toList();
                    return new PriorityGroupResponse(entry.getKey(), todoItems);
                })
                .toList();

        return new GroupedByPriorityResponse(date, priorities);
    }

    private Category findCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_NOT_FOUND));
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new CustomException(CategoryErrorCode.CATEGORY_INVALID_INPUT);
        }
    }
}
