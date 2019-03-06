package com.zsoft.service;

import com.zsoft.domain.Profile;
import com.zsoft.domain.User;
import com.zsoft.repository.ProfileRepository;
import com.zsoft.repository.search.ProfileSearchRepository;
import com.zsoft.service.dto.ProfileDTO;
import com.zsoft.service.mapper.ProfileMapper;
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
import com.zsoft.repository.UserRepository;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Profile.
 */
@Service
@Transactional
public class ProfileService {

    private final Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    private final ProfileSearchRepository profileSearchRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, ProfileMapper profileMapper, ProfileSearchRepository profileSearchRepository , UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.profileSearchRepository = profileSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a profile.
     *
     * @param profileDTO the entity to save
     * @return the persisted entity
     */
    public ProfileDTO save(ProfileDTO profileDTO) {
        log.debug("Request to save Profile : {}", profileDTO);

        Profile profile = profileMapper.toEntity(profileDTO);
        profile = profileRepository.save(profile);
        ProfileDTO result = profileMapper.toDto(profile);
        profileSearchRepository.save(profile);
        return result;
    }

    /**
     * Get all the profiles.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProfileDTO> findAll() {
        log.debug("Request to get all Profiles");
        String connectedUserLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userRepository.findOneByLogin(connectedUserLogin);
        return profileRepository.findAllByUser(currentUser.get())
            .stream()
            .map(profileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one profile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProfileDTO> findOne(Long id) {
        log.debug("Request to get Profile : {}", id);
        return profileRepository.findById(id)
            .map(profileMapper::toDto);
    }

    /**
     * Delete the profile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Profile : {}", id);
        profileRepository.deleteById(id);
        profileSearchRepository.deleteById(id);
    }

    /**
     * Search for the profile corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProfileDTO> search(String query) {
        log.debug("Request to search Profiles for query {}", query);
        return StreamSupport
            .stream(profileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(profileMapper::toDto)
            .collect(Collectors.toList());
    }
}
