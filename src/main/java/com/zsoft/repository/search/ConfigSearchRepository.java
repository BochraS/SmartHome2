package com.zsoft.repository.search;

import com.zsoft.domain.Config;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Config entity.
 */
public interface ConfigSearchRepository extends ElasticsearchRepository<Config, Long> {
}
