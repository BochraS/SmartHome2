package com.zsoft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zsoft.service.ConfigService;
import com.zsoft.web.rest.errors.BadRequestAlertException;
import com.zsoft.web.rest.util.HeaderUtil;
import com.zsoft.service.dto.ConfigDTO;
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
 * REST controller for managing Config.
 */
@RestController
@RequestMapping("/api")
public class ConfigResource {

    private final Logger log = LoggerFactory.getLogger(ConfigResource.class);

    private static final String ENTITY_NAME = "config";

    private final ConfigService configService;

    public ConfigResource(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * POST  /configs : Create a new config.
     *
     * @param configDTO the configDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configDTO, or with status 400 (Bad Request) if the config has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/configs")
    @Timed
    public ResponseEntity<ConfigDTO> createConfig(@RequestBody ConfigDTO configDTO) throws URISyntaxException {
        log.debug("REST request to save Config : {}", configDTO);
        if (configDTO.getId() != null) {
            throw new BadRequestAlertException("A new config cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigDTO result = configService.save(configDTO);
        return ResponseEntity.created(new URI("/api/configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /configs : Updates an existing config.
     *
     * @param configDTO the configDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configDTO,
     * or with status 400 (Bad Request) if the configDTO is not valid,
     * or with status 500 (Internal Server Error) if the configDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/configs")
    @Timed
    public ResponseEntity<ConfigDTO> updateConfig(@RequestBody ConfigDTO configDTO) throws URISyntaxException {
        log.debug("REST request to update Config : {}", configDTO);
        if (configDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigDTO result = configService.save(configDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, configDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /configs : get all the configs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of configs in body
     */
    @GetMapping("/configs")
    @Timed
    public List<ConfigDTO> getAllConfigs( long id) {
        log.debug("REST request to get all Configs");
        return configService.findAll( id);
    }

    /**
     * GET  /configs/:id : get the "id" config.
     *
     * @param id the id of the configDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configDTO, or with status 404 (Not Found)
     */
    @GetMapping("/configs/{id}")
    @Timed
    public ResponseEntity<ConfigDTO> getConfig(@PathVariable Long id) {
        log.debug("REST request to get Config : {}", id);
        Optional<ConfigDTO> configDTO = configService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configDTO);
    }

    /**
     * DELETE  /configs/:id : delete the "id" config.
     *
     * @param id the id of the configDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfig(@PathVariable Long id) {
        log.debug("REST request to delete Config : {}", id);
        configService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/configs?query=:query : search for the config corresponding
     * to the query.
     *
     * @param query the query of the config search
     * @return the result of the search
     */
    @GetMapping("/_search/configs")
    @Timed
    public List<ConfigDTO> searchConfigs(@RequestParam String query) {
        log.debug("REST request to search Configs for query {}", query);
        return configService.search(query);
    }

}
