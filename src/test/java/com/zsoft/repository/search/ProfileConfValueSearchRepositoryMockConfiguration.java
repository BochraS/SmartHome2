package com.zsoft.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ProfileConfValueSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ProfileConfValueSearchRepositoryMockConfiguration {

    @MockBean
    private ProfileConfValueSearchRepository mockProfileConfValueSearchRepository;

}
