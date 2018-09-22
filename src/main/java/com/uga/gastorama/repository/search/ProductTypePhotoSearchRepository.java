package com.uga.gastorama.repository.search;

import com.uga.gastorama.domain.ProductTypePhoto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProductTypePhoto entity.
 */
public interface ProductTypePhotoSearchRepository extends ElasticsearchRepository<ProductTypePhoto, Long> {
}
