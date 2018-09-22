package com.uga.gastorama.repository.search;

import com.uga.gastorama.domain.Confectioner;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Confectioner entity.
 */
public interface ConfectionerSearchRepository extends ElasticsearchRepository<Confectioner, Long> {
}
