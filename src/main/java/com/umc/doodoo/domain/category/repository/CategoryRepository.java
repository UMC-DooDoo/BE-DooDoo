package com.umc.doodoo.domain.category.repository;

import com.umc.doodoo.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByMemberId(Long memberId);

    Optional<Category> findByIdAndMemberId(Long id, Long memberId);
}
