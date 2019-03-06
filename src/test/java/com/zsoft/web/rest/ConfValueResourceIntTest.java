package com.zsoft.web.rest;

import com.zsoft.CraApp;

import com.zsoft.domain.ConfValue;
import com.zsoft.repository.ConfValueRepository;
import com.zsoft.repository.search.ConfValueSearchRepository;
import com.zsoft.service.ConfValueService;
import com.zsoft.service.dto.ConfValueDTO;
import com.zsoft.service.mapper.ConfValueMapper;
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
 * Test class for the ConfValueResource REST controller.
 *
 * @see ConfValueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CraApp.class)
public class ConfValueResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private ConfValueRepository confValueRepository;

    @Autowired
    private ConfValueMapper confValueMapper;

    @Autowired
    private ConfValueService confValueService;

    /**
     * This repository is mocked in the com.zsoft.repository.search test package.
     *
     * @see com.zsoft.repository.search.ConfValueSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfValueSearchRepository mockConfValueSearchRepository;

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

    private MockMvc restConfValueMockMvc;

    private ConfValue confValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfValueResource confValueResource = new ConfValueResource(confValueService);
        this.restConfValueMockMvc = MockMvcBuilders.standaloneSetup(confValueResource)
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
    public static ConfValue createEntity(EntityManager em) {
        ConfValue confValue = new ConfValue()
            .value(DEFAULT_VALUE);
        return confValue;
    }

    @Before
    public void initTest() {
        confValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfValue() throws Exception {
        int databaseSizeBeforeCreate = confValueRepository.findAll().size();

        // Create the ConfValue
        ConfValueDTO confValueDTO = confValueMapper.toDto(confValue);
        restConfValueMockMvc.perform(post("/api/conf-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confValueDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfValue in the database
        List<ConfValue> confValueList = confValueRepository.findAll();
        assertThat(confValueList).hasSize(databaseSizeBeforeCreate + 1);
        ConfValue testConfValue = confValueList.get(confValueList.size() - 1);
        assertThat(testConfValue.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the ConfValue in Elasticsearch
        verify(mockConfValueSearchRepository, times(1)).save(testConfValue);
    }

    @Test
    @Transactional
    public void createConfValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = confValueRepository.findAll().size();

        // Create the ConfValue with an existing ID
        confValue.setId(1L);
        ConfValueDTO confValueDTO = confValueMapper.toDto(confValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfValueMockMvc.perform(post("/api/conf-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfValue in the database
        List<ConfValue> confValueList = confValueRepository.findAll();
        assertThat(confValueList).hasSize(databaseSizeBeforeCreate);

        // Validate the ConfValue in Elasticsearch
        verify(mockConfValueSearchRepository, times(0)).save(confValue);
    }

    @Test
    @Transactional
    public void getAllConfValues() throws Exception {
        // Initialize the database
        confValueRepository.saveAndFlush(confValue);

        // Get all the confValueList
        restConfValueMockMvc.perform(get("/api/conf-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }
    
    @Test
    @Transactional
    public void getConfValue() throws Exception {
        // Initialize the database
        confValueRepository.saveAndFlush(confValue);

        // Get the confValue
        restConfValueMockMvc.perform(get("/api/conf-values/{id}", confValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(confValue.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfValue() throws Exception {
        // Get the confValue
        restConfValueMockMvc.perform(get("/api/conf-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfValue() throws Exception {
        // Initialize the database
        confValueRepository.saveAndFlush(confValue);

        int databaseSizeBeforeUpdate = confValueRepository.findAll().size();

        // Update the confValue
        ConfValue updatedConfValue = confValueRepository.findById(confValue.getId()).get();
        // Disconnect from session so that the updates on updatedConfValue are not directly saved in db
        em.detach(updatedConfValue);
        updatedConfValue
            .value(UPDATED_VALUE);
        ConfValueDTO confValueDTO = confValueMapper.toDto(updatedConfValue);

        restConfValueMockMvc.perform(put("/api/conf-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confValueDTO)))
            .andExpect(status().isOk());

        // Validate the ConfValue in the database
        List<ConfValue> confValueList = confValueRepository.findAll();
        assertThat(confValueList).hasSize(databaseSizeBeforeUpdate);
        ConfValue testConfValue = confValueList.get(confValueList.size() - 1);
        assertThat(testConfValue.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the ConfValue in Elasticsearch
        verify(mockConfValueSearchRepository, times(1)).save(testConfValue);
    }

    @Test
    @Transactional
    public void updateNonExistingConfValue() throws Exception {
        int databaseSizeBeforeUpdate = confValueRepository.findAll().size();

        // Create the ConfValue
        ConfValueDTO confValueDTO = confValueMapper.toDto(confValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfValueMockMvc.perform(put("/api/conf-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfValue in the database
        List<ConfValue> confValueList = confValueRepository.findAll();
        assertThat(confValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ConfValue in Elasticsearch
        verify(mockConfValueSearchRepository, times(0)).save(confValue);
    }

    @Test
    @Transactional
    public void deleteConfValue() throws Exception {
        // Initialize the database
        confValueRepository.saveAndFlush(confValue);

        int databaseSizeBeforeDelete = confValueRepository.findAll().size();

        // Get the confValue
        restConfValueMockMvc.perform(delete("/api/conf-values/{id}", confValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ConfValue> confValueList = confValueRepository.findAll();
        assertThat(confValueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ConfValue in Elasticsearch
        verify(mockConfValueSearchRepository, times(1)).deleteById(confValue.getId());
    }

    @Test
    @Transactional
    public void searchConfValue() throws Exception {
        // Initialize the database
        confValueRepository.saveAndFlush(confValue);
        when(mockConfValueSearchRepository.search(queryStringQuery("id:" + confValue.getId())))
            .thenReturn(Collections.singletonList(confValue));
        // Search the confValue
        restConfValueMockMvc.perform(get("/api/_search/conf-values?query=id:" + confValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfValue.class);
        ConfValue confValue1 = new ConfValue();
        confValue1.setId(1L);
        ConfValue confValue2 = new ConfValue();
        confValue2.setId(confValue1.getId());
        assertThat(confValue1).isEqualTo(confValue2);
        confValue2.setId(2L);
        assertThat(confValue1).isNotEqualTo(confValue2);
        confValue1.setId(null);
        assertThat(confValue1).isNotEqualTo(confValue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfValueDTO.class);
        ConfValueDTO confValueDTO1 = new ConfValueDTO();
        confValueDTO1.setId(1L);
        ConfValueDTO confValueDTO2 = new ConfValueDTO();
        assertThat(confValueDTO1).isNotEqualTo(confValueDTO2);
        confValueDTO2.setId(confValueDTO1.getId());
        assertThat(confValueDTO1).isEqualTo(confValueDTO2);
        confValueDTO2.setId(2L);
        assertThat(confValueDTO1).isNotEqualTo(confValueDTO2);
        confValueDTO1.setId(null);
        assertThat(confValueDTO1).isNotEqualTo(confValueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(confValueMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(confValueMapper.fromId(null)).isNull();
    }
}
