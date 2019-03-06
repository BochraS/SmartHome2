package com.zsoft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zsoft.service.ProfileConfValueService;
import com.zsoft.web.rest.errors.BadRequestAlertException;
import com.zsoft.web.rest.util.HeaderUtil;
import com.zsoft.service.dto.ProfileConfValueDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProfileConfValue.
 */
@RestController
@RequestMapping("/api")
public class ProfileConfValueResource {

    private final Logger log = LoggerFactory.getLogger(ProfileConfValueResource.class);

    private static final String ENTITY_NAME = "profileConfValue";

    private final ProfileConfValueService profileConfValueService;

    public ProfileConfValueResource(ProfileConfValueService profileConfValueService) {
        this.profileConfValueService = profileConfValueService;
    }

    /**
     * POST  /profile-conf-values : Create a new profileConfValue.
     *
     * @param profileConfValueDTO the profileConfValueDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new profileConfValueDTO, or with status 400 (Bad Request) if the profileConfValue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/profile-conf-values")
    @Timed
    public ResponseEntity<ProfileConfValueDTO> createProfileConfValue(@RequestBody ProfileConfValueDTO profileConfValueDTO) throws URISyntaxException {
        log.debug("REST request to save ProfileConfValue : {}", profileConfValueDTO);
        if (profileConfValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new profileConfValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfileConfValueDTO result = profileConfValueService.save(profileConfValueDTO);
        return ResponseEntity.created(new URI("/api/profile-conf-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /profile-conf-values : Updates an existing profileConfValue.
     *
     * @param profileConfValueDTO the profileConfValueDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated profileConfValueDTO,
     * or with status 400 (Bad Request) if the profileConfValueDTO is not valid,
     * or with status 500 (Internal Server Error) if the profileConfValueDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/profile-conf-values")
    @Timed
    public ResponseEntity<ProfileConfValueDTO> updateProfileConfValue(@RequestBody ProfileConfValueDTO profileConfValueDTO) throws URISyntaxException {
        log.debug("REST request to update ProfileConfValue : {}", profileConfValueDTO);
        if (profileConfValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProfileConfValueDTO result = profileConfValueService.save(profileConfValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profileConfValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /profile-conf-values : get all the profileConfValues.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of profileConfValues in body
     */
    @GetMapping("/profile-conf-values")
    @Timed
    public List<ProfileConfValueDTO> getAllProfileConfValues() {
        log.debug("REST request to get all ProfileConfValues");
        return profileConfValueService.findAll();
    }

    /**
     * GET  /profile-conf-values/:id : get the "id" profileConfValue.
     *
     * @param id the id of the profileConfValueDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the profileConfValueDTO, or with status 404 (Not Found)
     */
    @GetMapping("/profile-conf-values/{id}")
    @Timed
    public ResponseEntity<ProfileConfValueDTO> getProfileConfValue(@PathVariable Long id) {
        log.debug("REST request to get ProfileConfValue : {}", id);
        Optional<ProfileConfValueDTO> profileConfValueDTO = profileConfValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileConfValueDTO);
    }

    /**
     * DELETE  /profile-conf-values/:id : delete the "id" profileConfValue.
     *
     * @param id the id of the profileConfValueDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/profile-conf-values/{id}")
    @Timed
    public ResponseEntity<Void> deleteProfileConfValue(@PathVariable Long id) {
        log.debug("REST request to delete ProfileConfValue : {}", id);
        profileConfValueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/profile-conf-values?query=:query : search for the profileConfValue corresponding
     * to the query.
     *
     * @param query the query of the profileConfValue search
     * @return the result of the search
     */
    @GetMapping("/_search/profile-conf-values")
    @Timed
    public List<ProfileConfValueDTO> searchProfileConfValues(@RequestParam String query) {
        log.debug("REST request to search ProfileConfValues for query {}", query);
        return profileConfValueService.search(query);
    }

}
