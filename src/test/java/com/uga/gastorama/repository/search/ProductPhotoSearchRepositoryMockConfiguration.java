package com.uga.gastorama.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ProductPhotoSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ProductPhotoSearchRepositoryMockConfiguration {

    @MockBean
    private ProductPhotoSearchRepository mockProductPhotoSearchRepository;

}
