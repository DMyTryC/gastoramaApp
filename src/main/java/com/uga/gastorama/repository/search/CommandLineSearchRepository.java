package com.uga.gastorama.repository.search;

import com.uga.gastorama.domain.CommandLine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CommandLine entity.
 */
public interface CommandLineSearchRepository extends ElasticsearchRepository<CommandLine, Long> {
}
