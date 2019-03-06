package com.zsoft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zsoft.service.ConfValueService;
import com.zsoft.web.rest.errors.BadRequestAlertException;
import com.zsoft.web.rest.util.HeaderUtil;
import com.zsoft.service.dto.ConfValueDTO;
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
 * REST controller for managing ConfValue.
 */
@RestController
@RequestMapping("/api")
public class ConfValueResource {

    private final Logger log = LoggerFactory.getLogger(ConfValueResource.class);

    private static final String ENTITY_NAME = "confValue";

    private final ConfValueService confValueService;

    public ConfValueResource(ConfValueService confValueService) {
        this.confValueService = confValueService;
    }

    /**
     * POST  /conf-values : Create a new confValue.
     *
     * @param confValueDTO the confValueDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new confValueDTO, or with status 400 (Bad Request) if the confValue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/conf-values")
    @Timed
    public ResponseEntity<ConfValueDTO> createConfValue(@RequestBody ConfValueDTO confValueDTO) throws URISyntaxException {
        log.debug("REST request to save ConfValue : {}", confValueDTO);
        if (confValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new confValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfValueDTO result = confValueService.save(confValueDTO);
        return ResponseEntity.created(new URI("/api/conf-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /conf-values : Updates an existing confValue.
     *
     * @param confValueDTO the confValueDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated confValueDTO,
     * or with status 400 (Bad Request) if the confValueDTO is not valid,
     * or with status 500 (Internal Server Error) if the confValueDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/conf-values")
    @Timed
    public ResponseEntity<ConfValueDTO> updateConfValue(@RequestBody ConfValueDTO confValueDTO) throws URISyntaxException {
        log.debug("REST request to update ConfValue : {}", confValueDTO);
        if (confValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfValueDTO result = confValueService.save(confValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, confValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /conf-values : get all the confValues.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of confValues in body
     */
    @GetMapping("/conf-values")
    @Timed
    public List<ConfValueDTO> getAllConfValues() {
        log.debug("REST request to get all ConfValues");
        return confValueService.findAll();
    }

    /**
     * GET  /conf-values/:id : get the "id" confValue.
     *
     * @param id the id of the confValueDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the confValueDTO, or with status 404 (Not Found)
     */
    @GetMapping("/conf-values/{id}")
    @Timed
    public ResponseEntity<ConfValueDTO> getConfValue(@PathVariable Long id) {
        log.debug("REST request to get ConfValue : {}", id);
        Optional<ConfValueDTO> confValueDTO = confValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(confValueDTO);
    }

    /**
     * DELETE  /conf-values/:id : delete the "id" confValue.
     *
     * @param id the id of the confValueDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/conf-values/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfValue(@PathVariable Long id) {
        log.debug("REST request to delete ConfValue : {}", id);
        confValueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/conf-values?query=:query : search for the confValue corresponding
     * to the query.
     *
     * @param query the query of the confValue search
     * @return the result of the search
     */
    @GetMapping("/_search/conf-values")
    @Timed
    public List<ConfValueDTO> searchConfValues(@RequestParam String query) {
        log.debug("REST request to search ConfValues for query {}", query);
        return confValueService.search(query);
    }

}
