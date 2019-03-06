package com.zsoft.repository.search;

import com.zsoft.domain.SmartObject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SmartObject entity.
 */
public interface SmartObjectSearchRepository extends ElasticsearchRepository<SmartObject, Long> {
}
