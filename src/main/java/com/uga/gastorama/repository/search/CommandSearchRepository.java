package com.uga.gastorama.repository.search;

import com.uga.gastorama.domain.Command;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Command entity.
 */
public interface CommandSearchRepository extends ElasticsearchRepository<Command, Long> {
}
