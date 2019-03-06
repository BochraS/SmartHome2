package com.zsoft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zsoft.service.SmartObjectService;
import com.zsoft.web.rest.errors.BadRequestAlertException;
import com.zsoft.web.rest.util.HeaderUtil;
import com.zsoft.service.dto.SmartObjectDTO;
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
 * REST controller for managing SmartObject.
 */
@RestController
@RequestMapping("/api")
public class SmartObjectResource {

    private final Logger log = LoggerFactory.getLogger(SmartObjectResource.class);

    private static final String ENTITY_NAME = "smartObject";

    private final SmartObjectService smartObjectService;

    public SmartObjectResource(SmartObjectService smartObjectService) {
        this.smartObjectService = smartObjectService;
    }

    /**
     * POST  /smart-objects : Create a new smartObject.
     *
     * @param smartObjectDTO the smartObjectDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smartObjectDTO, or with status 400 (Bad Request) if the smartObject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smart-objects")
    @Timed
    public ResponseEntity<SmartObjectDTO> createSmartObject(@RequestBody SmartObjectDTO smartObjectDTO) throws URISyntaxException {
        log.debug("REST request to save SmartObject : {}", smartObjectDTO);
        if (smartObjectDTO.getId() != null) {
            throw new BadRequestAlertException("A new smartObject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmartObjectDTO result = smartObjectService.save(smartObjectDTO);
        return ResponseEntity.created(new URI("/api/smart-objects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smart-objects : Updates an existing smartObject.
     *
     * @param smartObjectDTO the smartObjectDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smartObjectDTO,
     * or with status 400 (Bad Request) if the smartObjectDTO is not valid,
     * or with status 500 (Internal Server Error) if the smartObjectDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smart-objects")
    @Timed
    public ResponseEntity<SmartObjectDTO> updateSmartObject(@RequestBody SmartObjectDTO smartObjectDTO) throws URISyntaxException {
        log.debug("REST request to update SmartObject : {}", smartObjectDTO);
        if (smartObjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmartObjectDTO result = smartObjectService.save(smartObjectDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smartObjectDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smart-objects : get all the smartObjects.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smartObjects in body
     */
    @GetMapping("/smart-objects")
    @Timed
    public List<SmartObjectDTO> getAllSmartObjects() {
        log.debug("REST request to get all SmartObjects");
        return smartObjectService.findAll();
    }

    /**
     * GET  /smart-objects/:id : get the "id" smartObject.
     *
     * @param id the id of the smartObjectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smartObjectDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smart-objects/{id}")
    @Timed
    public ResponseEntity<SmartObjectDTO> getSmartObject(@PathVariable Long id) {
        log.debug("REST request to get SmartObject : {}", id);
        Optional<SmartObjectDTO> smartObjectDTO = smartObjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smartObjectDTO);
    }

    /**
     * DELETE  /smart-objects/:id : delete the "id" smartObject.
     *
     * @param id the id of the smartObjectDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smart-objects/{id}")
    @Timed
    public ResponseEntity<Void> deleteSmartObject(@PathVariable Long id) {
        log.debug("REST request to delete SmartObject : {}", id);
        smartObjectService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/smart-objects?query=:query : search for the smartObject corresponding
     * to the query.
     *
     * @param query the query of the smartObject search
     * @return the result of the search
     */
    @GetMapping("/_search/smart-objects")
    @Timed
    public List<SmartObjectDTO> searchSmartObjects(@RequestParam String query) {
        log.debug("REST request to search SmartObjects for query {}", query);
        return smartObjectService.search(query);
    }

}
