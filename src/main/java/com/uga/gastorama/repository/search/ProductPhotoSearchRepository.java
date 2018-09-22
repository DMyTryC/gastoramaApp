package com.uga.gastorama.repository.search;

import com.uga.gastorama.domain.ProductPhoto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProductPhoto entity.
 */
public interface ProductPhotoSearchRepository extends ElasticsearchRepository<ProductPhoto, Long> {
}
