package com.zsoft.service;

import com.zsoft.domain.ConfValue;
import com.zsoft.repository.ConfValueRepository;
import com.zsoft.repository.search.ConfValueSearchRepository;
import com.zsoft.service.dto.ConfValueDTO;
import com.zsoft.service.mapper.ConfValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ConfValue.
 */
@Service
@Transactional
public class ConfValueService {

    private final Logger log = LoggerFactory.getLogger(ConfValueService.class);

    private final ConfValueRepository confValueRepository;

    private final ConfValueMapper confValueMapper;

    private final ConfValueSearchRepository confValueSearchRepository;

    public ConfValueService(ConfValueRepository confValueRepository, ConfValueMapper confValueMapper, ConfValueSearchRepository confValueSearchRepository) {
        this.confValueRepository = confValueRepository;
        this.confValueMapper = confValueMapper;
        this.confValueSearchRepository = confValueSearchRepository;
    }

    /**
     * Save a confValue.
     *
     * @param confValueDTO the entity to save
     * @return the persisted entity
     */
    public ConfValueDTO save(ConfValueDTO confValueDTO) {
        log.debug("Request to save ConfValue : {}", confValueDTO);

        ConfValue confValue = confValueMapper.toEntity(confValueDTO);
        confValue = confValueRepository.save(confValue);
        ConfValueDTO result = confValueMapper.toDto(confValue);
        confValueSearchRepository.save(confValue);
        return result;
    }

    /**
     * Get all the confValues.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ConfValueDTO> findAll() {
        log.debug("Request to get all ConfValues");
        return confValueRepository.findAll().stream()
            .map(confValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one confValue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ConfValueDTO> findOne(Long id) {
        log.debug("Request to get ConfValue : {}", id);
        return confValueRepository.findById(id)
            .map(confValueMapper::toDto);
    }

    /**
     * Delete the confValue by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ConfValue : {}", id);
        confValueRepository.deleteById(id);
        confValueSearchRepository.deleteById(id);
    }

    /**
     * Search for the confValue corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ConfValueDTO> search(String query) {
        log.debug("Request to search ConfValues for query {}", query);
        return StreamSupport
            .stream(confValueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(confValueMapper::toDto)
            .collect(Collectors.toList());
    }
}
