package com.zsoft.service;

import com.zsoft.domain.ProfileConfValue;
import com.zsoft.repository.ProfileConfValueRepository;
import com.zsoft.repository.search.ProfileConfValueSearchRepository;
import com.zsoft.service.dto.ProfileConfValueDTO;
import com.zsoft.service.mapper.ProfileConfValueMapper;
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
 * Service Implementation for managing ProfileConfValue.
 */
@Service
@Transactional
public class ProfileConfValueService {

    private final Logger log = LoggerFactory.getLogger(ProfileConfValueService.class);

    private final ProfileConfValueRepository profileConfValueRepository;

    private final ProfileConfValueMapper profileConfValueMapper;

    private final ProfileConfValueSearchRepository profileConfValueSearchRepository;

    public ProfileConfValueService(ProfileConfValueRepository profileConfValueRepository, ProfileConfValueMapper profileConfValueMapper, ProfileConfValueSearchRepository profileConfValueSearchRepository) {
        this.profileConfValueRepository = profileConfValueRepository;
        this.profileConfValueMapper = profileConfValueMapper;
        this.profileConfValueSearchRepository = profileConfValueSearchRepository;
    }

    /**
     * Save a profileConfValue.
     *
     * @param profileConfValueDTO the entity to save
     * @return the persisted entity
     */
    public ProfileConfValueDTO save(ProfileConfValueDTO profileConfValueDTO) {
        log.debug("Request to save ProfileConfValue : {}", profileConfValueDTO);

        ProfileConfValue profileConfValue = profileConfValueMapper.toEntity(profileConfValueDTO);
        profileConfValue = profileConfValueRepository.save(profileConfValue);
        ProfileConfValueDTO result = profileConfValueMapper.toDto(profileConfValue);
        profileConfValueSearchRepository.save(profileConfValue);
        return result;
    }

    /**
     * Get all the profileConfValues.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProfileConfValueDTO> findAll() {
        log.debug("Request to get all ProfileConfValues");
        return profileConfValueRepository.findAll().stream()
            .map(profileConfValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one profileConfValue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProfileConfValueDTO> findOne(Long id) {
        log.debug("Request to get ProfileConfValue : {}", id);
        return profileConfValueRepository.findById(id)
            .map(profileConfValueMapper::toDto);
    }

    /**
     * Delete the profileConfValue by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProfileConfValue : {}", id);
        profileConfValueRepository.deleteById(id);
        profileConfValueSearchRepository.deleteById(id);
    }

    /**
     * Search for the profileConfValue corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProfileConfValueDTO> search(String query) {
        log.debug("Request to search ProfileConfValues for query {}", query);
        return StreamSupport
            .stream(profileConfValueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(profileConfValueMapper::toDto)
            .collect(Collectors.toList());
    }
}
