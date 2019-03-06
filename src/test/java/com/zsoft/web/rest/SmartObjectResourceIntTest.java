package com.zsoft.web.rest;

import com.zsoft.CraApp;

import com.zsoft.domain.SmartObject;
import com.zsoft.repository.SmartObjectRepository;
import com.zsoft.repository.search.SmartObjectSearchRepository;
import com.zsoft.service.SmartObjectService;
import com.zsoft.service.dto.SmartObjectDTO;
import com.zsoft.service.mapper.SmartObjectMapper;
import com.zsoft.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.zsoft.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SmartObjectResource REST controller.
 *
 * @see SmartObjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CraApp.class)
public class SmartObjectResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHARED = false;
    private static final Boolean UPDATED_SHARED = true;

    private static final String DEFAULT_FABRIQUANT = "AAAAAAAAAA";
    private static final String UPDATED_FABRIQUANT = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR_OF_FABRIQUATION = "AAAAAAAAAA";
    private static final String UPDATED_YEAR_OF_FABRIQUATION = "BBBBBBBBBB";

    @Autowired
    private SmartObjectRepository smartObjectRepository;

    @Autowired
    private SmartObjectMapper smartObjectMapper;

    @Autowired
    private SmartObjectService smartObjectService;

    /**
     * This repository is mocked in the com.zsoft.repository.search test package.
     *
     * @see com.zsoft.repository.search.SmartObjectSearchRepositoryMockConfiguration
     */
    @Autowired
    private SmartObjectSearchRepository mockSmartObjectSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSmartObjectMockMvc;

    private SmartObject smartObject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmartObjectResource smartObjectResource = new SmartObjectResource(smartObjectService);
        this.restSmartObjectMockMvc = MockMvcBuilders.standaloneSetup(smartObjectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmartObject createEntity(EntityManager em) {
        SmartObject smartObject = new SmartObject()
            .type(DEFAULT_TYPE)
            .shared(DEFAULT_SHARED)
            .fabriquant(DEFAULT_FABRIQUANT)
            .yearOfFabriquation(DEFAULT_YEAR_OF_FABRIQUATION);
        return smartObject;
    }

    @Before
    public void initTest() {
        smartObject = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmartObject() throws Exception {
        int databaseSizeBeforeCreate = smartObjectRepository.findAll().size();

        // Create the SmartObject
        SmartObjectDTO smartObjectDTO = smartObjectMapper.toDto(smartObject);
        restSmartObjectMockMvc.perform(post("/api/smart-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartObjectDTO)))
            .andExpect(status().isCreated());

        // Validate the SmartObject in the database
        List<SmartObject> smartObjectList = smartObjectRepository.findAll();
        assertThat(smartObjectList).hasSize(databaseSizeBeforeCreate + 1);
        SmartObject testSmartObject = smartObjectList.get(smartObjectList.size() - 1);
        assertThat(testSmartObject.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSmartObject.isShared()).isEqualTo(DEFAULT_SHARED);
        assertThat(testSmartObject.getFabriquant()).isEqualTo(DEFAULT_FABRIQUANT);
        assertThat(testSmartObject.getYearOfFabriquation()).isEqualTo(DEFAULT_YEAR_OF_FABRIQUATION);

        // Validate the SmartObject in Elasticsearch
        verify(mockSmartObjectSearchRepository, times(1)).save(testSmartObject);
    }

    @Test
    @Transactional
    public void createSmartObjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smartObjectRepository.findAll().size();

        // Create the SmartObject with an existing ID
        smartObject.setId(1L);
        SmartObjectDTO smartObjectDTO = smartObjectMapper.toDto(smartObject);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmartObjectMockMvc.perform(post("/api/smart-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartObjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmartObject in the database
        List<SmartObject> smartObjectList = smartObjectRepository.findAll();
        assertThat(smartObjectList).hasSize(databaseSizeBeforeCreate);

        // Validate the SmartObject in Elasticsearch
        verify(mockSmartObjectSearchRepository, times(0)).save(smartObject);
    }

    @Test
    @Transactional
    public void getAllSmartObjects() throws Exception {
        // Initialize the database
        smartObjectRepository.saveAndFlush(smartObject);

        // Get all the smartObjectList
        restSmartObjectMockMvc.perform(get("/api/smart-objects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smartObject.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].shared").value(hasItem(DEFAULT_SHARED.booleanValue())))
            .andExpect(jsonPath("$.[*].fabriquant").value(hasItem(DEFAULT_FABRIQUANT.toString())))
            .andExpect(jsonPath("$.[*].yearOfFabriquation").value(hasItem(DEFAULT_YEAR_OF_FABRIQUATION.toString())));
    }
    
    @Test
    @Transactional
    public void getSmartObject() throws Exception {
        // Initialize the database
        smartObjectRepository.saveAndFlush(smartObject);

        // Get the smartObject
        restSmartObjectMockMvc.perform(get("/api/smart-objects/{id}", smartObject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smartObject.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.shared").value(DEFAULT_SHARED.booleanValue()))
            .andExpect(jsonPath("$.fabriquant").value(DEFAULT_FABRIQUANT.toString()))
            .andExpect(jsonPath("$.yearOfFabriquation").value(DEFAULT_YEAR_OF_FABRIQUATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSmartObject() throws Exception {
        // Get the smartObject
        restSmartObjectMockMvc.perform(get("/api/smart-objects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmartObject() throws Exception {
        // Initialize the database
        smartObjectRepository.saveAndFlush(smartObject);

        int databaseSizeBeforeUpdate = smartObjectRepository.findAll().size();

        // Update the smartObject
        SmartObject updatedSmartObject = smartObjectRepository.findById(smartObject.getId()).get();
        // Disconnect from session so that the updates on updatedSmartObject are not directly saved in db
        em.detach(updatedSmartObject);
        updatedSmartObject
            .type(UPDATED_TYPE)
            .shared(UPDATED_SHARED)
            .fabriquant(UPDATED_FABRIQUANT)
            .yearOfFabriquation(UPDATED_YEAR_OF_FABRIQUATION);
        SmartObjectDTO smartObjectDTO = smartObjectMapper.toDto(updatedSmartObject);

        restSmartObjectMockMvc.perform(put("/api/smart-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartObjectDTO)))
            .andExpect(status().isOk());

        // Validate the SmartObject in the database
        List<SmartObject> smartObjectList = smartObjectRepository.findAll();
        assertThat(smartObjectList).hasSize(databaseSizeBeforeUpdate);
        SmartObject testSmartObject = smartObjectList.get(smartObjectList.size() - 1);
        assertThat(testSmartObject.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSmartObject.isShared()).isEqualTo(UPDATED_SHARED);
        assertThat(testSmartObject.getFabriquant()).isEqualTo(UPDATED_FABRIQUANT);
        assertThat(testSmartObject.getYearOfFabriquation()).isEqualTo(UPDATED_YEAR_OF_FABRIQUATION);

        // Validate the SmartObject in Elasticsearch
        verify(mockSmartObjectSearchRepository, times(1)).save(testSmartObject);
    }

    @Test
    @Transactional
    public void updateNonExistingSmartObject() throws Exception {
        int databaseSizeBeforeUpdate = smartObjectRepository.findAll().size();

        // Create the SmartObject
        SmartObjectDTO smartObjectDTO = smartObjectMapper.toDto(smartObject);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmartObjectMockMvc.perform(put("/api/smart-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartObjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmartObject in the database
        List<SmartObject> smartObjectList = smartObjectRepository.findAll();
        assertThat(smartObjectList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SmartObject in Elasticsearch
        verify(mockSmartObjectSearchRepository, times(0)).save(smartObject);
    }

    @Test
    @Transactional
    public void deleteSmartObject() throws Exception {
        // Initialize the database
        smartObjectRepository.saveAndFlush(smartObject);

        int databaseSizeBeforeDelete = smartObjectRepository.findAll().size();

        // Get the smartObject
        restSmartObjectMockMvc.perform(delete("/api/smart-objects/{id}", smartObject.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmartObject> smartObjectList = smartObjectRepository.findAll();
        assertThat(smartObjectList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SmartObject in Elasticsearch
        verify(mockSmartObjectSearchRepository, times(1)).deleteById(smartObject.getId());
    }

    @Test
    @Transactional
    public void searchSmartObject() throws Exception {
        // Initialize the database
        smartObjectRepository.saveAndFlush(smartObject);
        when(mockSmartObjectSearchRepository.search(queryStringQuery("id:" + smartObject.getId())))
            .thenReturn(Collections.singletonList(smartObject));
        // Search the smartObject
        restSmartObjectMockMvc.perform(get("/api/_search/smart-objects?query=id:" + smartObject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smartObject.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].shared").value(hasItem(DEFAULT_SHARED.booleanValue())))
            .andExpect(jsonPath("$.[*].fabriquant").value(hasItem(DEFAULT_FABRIQUANT)))
            .andExpect(jsonPath("$.[*].yearOfFabriquation").value(hasItem(DEFAULT_YEAR_OF_FABRIQUATION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmartObject.class);
        SmartObject smartObject1 = new SmartObject();
        smartObject1.setId(1L);
        SmartObject smartObject2 = new SmartObject();
        smartObject2.setId(smartObject1.getId());
        assertThat(smartObject1).isEqualTo(smartObject2);
        smartObject2.setId(2L);
        assertThat(smartObject1).isNotEqualTo(smartObject2);
        smartObject1.setId(null);
        assertThat(smartObject1).isNotEqualTo(smartObject2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmartObjectDTO.class);
        SmartObjectDTO smartObjectDTO1 = new SmartObjectDTO();
        smartObjectDTO1.setId(1L);
        SmartObjectDTO smartObjectDTO2 = new SmartObjectDTO();
        assertThat(smartObjectDTO1).isNotEqualTo(smartObjectDTO2);
        smartObjectDTO2.setId(smartObjectDTO1.getId());
        assertThat(smartObjectDTO1).isEqualTo(smartObjectDTO2);
        smartObjectDTO2.setId(2L);
        assertThat(smartObjectDTO1).isNotEqualTo(smartObjectDTO2);
        smartObjectDTO1.setId(null);
        assertThat(smartObjectDTO1).isNotEqualTo(smartObjectDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smartObjectMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smartObjectMapper.fromId(null)).isNull();
    }
}
