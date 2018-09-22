package com.uga.gastorama.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CommandLineSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CommandLineSearchRepositoryMockConfiguration {

    @MockBean
    private CommandLineSearchRepository mockCommandLineSearchRepository;

}
