package com.uga.gastorama.repository;

import com.uga.gastorama.domain.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Ingredient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    @Query(value = "select distinct ingredient from Ingredient ingredient left join fetch ingredient.products",
        countQuery = "select count(distinct ingredient) from Ingredient ingredient")
    Page<Ingredient> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct ingredient from Ingredient ingredient left join fetch ingredient.products")
    List<Ingredient> findAllWithEagerRelationships();

    @Query("select ingredient from Ingredient ingredient left join fetch ingredient.products where ingredient.id =:id")
    Optional<Ingredient> findOneWithEagerRelationships(@Param("id") Long id);

}
