package com.zsoft.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ConfValueSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ConfValueSearchRepositoryMockConfiguration {

    @MockBean
    private ConfValueSearchRepository mockConfValueSearchRepository;

}
