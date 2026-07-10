package com.umc.doodoo.domain.category.entity;

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

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "category_name", length = 30, nullable = false)
    private String categoryName;

    @Convert(converter = ColorConverter.class)
    @Column(name = "color", nullable = false)
    private Color color;

    @Builder
    public Category(Long memberId, String categoryName, Color color) {
        this.memberId = memberId;
        this.categoryName = categoryName;
        this.color = color;
    }

    public void updateCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void updateColor(Color color) {
        this.color = color;
    }
}
