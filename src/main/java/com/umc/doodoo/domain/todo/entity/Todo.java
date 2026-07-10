package com.umc.doodoo.domain.todo.entity;

import com.umc.doodoo.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "todo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Column(name = "task_date", nullable = false)
    private LocalDate taskDate;

    @Convert(converter = PriorityConverter.class)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Column(name = "is_completed", nullable = false)
    private boolean completed;

    @Builder
    public Todo(Long userId, Long categoryId, String title, LocalDate taskDate, Priority priority) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.taskDate = taskDate;
        this.priority = priority;
        this.completed = false;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void updateTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }

    public void updatePriority(Priority priority) {
        this.priority = priority;
    }

    public void toggleComplete() {
        this.completed = !this.completed;
    }
}
