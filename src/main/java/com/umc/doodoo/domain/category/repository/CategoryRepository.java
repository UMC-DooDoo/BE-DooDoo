package com.umc.doodoo.domain.category.repository;

import com.umc.doodoo.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByMemberId(Long memberId);

    boolean existsByIdAndMemberId(Long id, Long memberId);
}
