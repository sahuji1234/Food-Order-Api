package com.zosh.repository;

import com.zosh.model.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory,Long> {

    List<IngredientCategory> findByRestaurantId(Long id);
}
