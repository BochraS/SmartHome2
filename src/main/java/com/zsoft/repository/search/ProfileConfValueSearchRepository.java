package com.zsoft.repository.search;

import com.zsoft.domain.ProfileConfValue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProfileConfValue entity.
 */
public interface ProfileConfValueSearchRepository extends ElasticsearchRepository<ProfileConfValue, Long> {
}
