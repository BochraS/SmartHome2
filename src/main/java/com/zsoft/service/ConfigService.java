package com.zsoft.service;

import com.zsoft.domain.Config;
import com.zsoft.domain.SmartObject;
import com.zsoft.domain.User;
import com.zsoft.repository.ConfigRepository;
import com.zsoft.repository.UserRepository;
import com.zsoft.repository.search.ConfigSearchRepository;
import com.zsoft.service.dto.ConfigDTO;
import com.zsoft.service.mapper.ConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;
import com.zsoft.repository.SmartObjectRepository;


/**
 * Service Implementation for managing Config.
 */
@Service
@Transactional
public class ConfigService {

    private final Logger log = LoggerFactory.getLogger(ConfigService.class);

    private final ConfigRepository configRepository;

    private final ConfigMapper configMapper;

    private final ConfigSearchRepository configSearchRepository;

    public ConfigService(ConfigRepository configRepository, ConfigMapper configMapper, ConfigSearchRepository configSearchRepository ) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
        this.configSearchRepository = configSearchRepository;

    }

    /**
     * Save a config.
     *
     * @param configDTO the entity to save
     * @return the persisted entity
     */
    public ConfigDTO save(ConfigDTO configDTO) {
        log.debug("Request to save Config : {}", configDTO);

        Config config = configMapper.toEntity(configDTO);
        config = configRepository.save(config);
        ConfigDTO result = configMapper.toDto(config);
        configSearchRepository.save(config);
        return result;
    }

    /**
     * Get all the configs.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ConfigDTO> findAll(long id) {
        log.debug("Request to get all Configs");

        return configRepository.findAllBySmartObject(id).stream()
            .map(configMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
    @Transactional(readOnly = true)
    public List<ConfigDTO> findbyobject( long id) {
        log.debug("Request to get all Configs");

        return configRepository.findAllBySmartObject(id).stream()
                .map(configMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one config by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ConfigDTO> findOne(Long id) {
        log.debug("Request to get Config : {}", id);
        return configRepository.findById(id)
            .map(configMapper::toDto);
    }

    /**
     * Delete the config by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Config : {}", id);
        configRepository.deleteById(id);
        configSearchRepository.deleteById(id);
    }

    /**
     * Search for the config corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ConfigDTO> search(String query) {
        log.debug("Request to search Configs for query {}", query);
        return StreamSupport
            .stream(configSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(configMapper::toDto)
            .collect(Collectors.toList());
    }
}
