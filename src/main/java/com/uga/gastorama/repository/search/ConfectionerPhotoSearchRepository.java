package com.uga.gastorama.repository.search;

import com.uga.gastorama.domain.ConfectionerPhoto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ConfectionerPhoto entity.
 */
public interface ConfectionerPhotoSearchRepository extends ElasticsearchRepository<ConfectionerPhoto, Long> {
}
