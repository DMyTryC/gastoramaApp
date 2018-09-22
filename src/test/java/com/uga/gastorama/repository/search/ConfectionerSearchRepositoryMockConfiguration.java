package com.uga.gastorama.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ConfectionerSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ConfectionerSearchRepositoryMockConfiguration {

    @MockBean
    private ConfectionerSearchRepository mockConfectionerSearchRepository;

}
