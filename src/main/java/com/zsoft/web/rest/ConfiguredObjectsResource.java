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
public class ConfiguredObjectsResource {
}
