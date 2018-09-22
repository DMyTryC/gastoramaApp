package com.uga.gastorama.repository.search;

import com.uga.gastorama.domain.Ingredient;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Ingredient entity.
 */
public interface IngredientSearchRepository extends ElasticsearchRepository<Ingredient, Long> {
}
