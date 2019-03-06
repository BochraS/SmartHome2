package com.zsoft.service;

import com.zsoft.domain.SmartObject;
import com.zsoft.domain.User;
import com.zsoft.repository.SmartObjectRepository;
import com.zsoft.repository.UserRepository;
import com.zsoft.repository.search.SmartObjectSearchRepository;
import com.zsoft.service.dto.SmartObjectDTO;
import com.zsoft.service.mapper.SmartObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SmartObject.
 */
@Service
@Transactional
public class SmartObjectService {

    private final Logger log = LoggerFactory.getLogger(SmartObjectService.class);

    private final SmartObjectRepository smartObjectRepository;

    private final SmartObjectMapper smartObjectMapper;

    private final SmartObjectSearchRepository smartObjectSearchRepository;

    private final UserRepository userRepository;

    public SmartObjectService(SmartObjectRepository smartObjectRepository, SmartObjectMapper smartObjectMapper, SmartObjectSearchRepository smartObjectSearchRepository, UserRepository userRepository) {
        this.smartObjectRepository = smartObjectRepository;
        this.smartObjectMapper = smartObjectMapper;
        this.smartObjectSearchRepository = smartObjectSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a smartObject.
     *
     * @param smartObjectDTO the entity to save
     * @return the persisted entity
     */
    public SmartObjectDTO save(SmartObjectDTO smartObjectDTO) {
        log.debug("Request to save SmartObject : {}", smartObjectDTO);

        SmartObject smartObject = smartObjectMapper.toEntity(smartObjectDTO);
        smartObject = smartObjectRepository.save(smartObject);
        SmartObjectDTO result = smartObjectMapper.toDto(smartObject);
        smartObjectSearchRepository.save(smartObject);
        return result;
    }

    /**
     * Get all the smartObjects.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SmartObjectDTO> findAll() {
        log.debug("Request to get all SmartObjects");

        String connectedUserLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userRepository.findOneByLogin(connectedUserLogin);

        return smartObjectRepository.findAllByUser(currentUser.get())
            .stream().map(smartObjectMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one smartObject by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SmartObjectDTO> findOne(Long id) {
        log.debug("Request to get SmartObject : {}", id);
        return smartObjectRepository.findById(id)
            .map(smartObjectMapper::toDto);
    }

    /**
     * Delete the smartObject by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SmartObject : {}", id);
        smartObjectRepository.deleteById(id);
        smartObjectSearchRepository.deleteById(id);
    }

    /**
     * Search for the smartObject corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SmartObjectDTO> search(String query) {
        log.debug("Request to search SmartObjects for query {}", query);
        return StreamSupport
            .stream(smartObjectSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(smartObjectMapper::toDto)
            .collect(Collectors.toList());
    }
}
