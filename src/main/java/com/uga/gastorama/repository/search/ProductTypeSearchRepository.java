package com.uga.gastorama.repository.search;

import com.uga.gastorama.domain.ProductType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProductType entity.
 */
public interface ProductTypeSearchRepository extends ElasticsearchRepository<ProductType, Long> {
}
