package com.zsoft.repository.search;

import com.zsoft.domain.ConfValue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ConfValue entity.
 */
public interface ConfValueSearchRepository extends ElasticsearchRepository<ConfValue, Long> {
}
