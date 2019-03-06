package com.zsoft.web.rest;

import com.zsoft.CraApp;

import com.zsoft.domain.ProfileConfValue;
import com.zsoft.repository.ProfileConfValueRepository;
import com.zsoft.repository.search.ProfileConfValueSearchRepository;
import com.zsoft.service.ProfileConfValueService;
import com.zsoft.service.dto.ProfileConfValueDTO;
import com.zsoft.service.mapper.ProfileConfValueMapper;
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
 * Test class for the ProfileConfValueResource REST controller.
 *
 * @see ProfileConfValueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CraApp.class)
public class ProfileConfValueResourceIntTest {

    @Autowired
    private ProfileConfValueRepository profileConfValueRepository;

    @Autowired
    private ProfileConfValueMapper profileConfValueMapper;

    @Autowired
    private ProfileConfValueService profileConfValueService;

    /**
     * This repository is mocked in the com.zsoft.repository.search test package.
     *
     * @see com.zsoft.repository.search.ProfileConfValueSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProfileConfValueSearchRepository mockProfileConfValueSearchRepository;

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

    private MockMvc restProfileConfValueMockMvc;

    private ProfileConfValue profileConfValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfileConfValueResource profileConfValueResource = new ProfileConfValueResource(profileConfValueService);
        this.restProfileConfValueMockMvc = MockMvcBuilders.standaloneSetup(profileConfValueResource)
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
    public static ProfileConfValue createEntity(EntityManager em) {
        ProfileConfValue profileConfValue = new ProfileConfValue();
        return profileConfValue;
    }

    @Before
    public void initTest() {
        profileConfValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfileConfValue() throws Exception {
        int databaseSizeBeforeCreate = profileConfValueRepository.findAll().size();

        // Create the ProfileConfValue
        ProfileConfValueDTO profileConfValueDTO = profileConfValueMapper.toDto(profileConfValue);
        restProfileConfValueMockMvc.perform(post("/api/profile-conf-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileConfValueDTO)))
            .andExpect(status().isCreated());

        // Validate the ProfileConfValue in the database
        List<ProfileConfValue> profileConfValueList = profileConfValueRepository.findAll();
        assertThat(profileConfValueList).hasSize(databaseSizeBeforeCreate + 1);
        ProfileConfValue testProfileConfValue = profileConfValueList.get(profileConfValueList.size() - 1);

        // Validate the ProfileConfValue in Elasticsearch
        verify(mockProfileConfValueSearchRepository, times(1)).save(testProfileConfValue);
    }

    @Test
    @Transactional
    public void createProfileConfValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileConfValueRepository.findAll().size();

        // Create the ProfileConfValue with an existing ID
        profileConfValue.setId(1L);
        ProfileConfValueDTO profileConfValueDTO = profileConfValueMapper.toDto(profileConfValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileConfValueMockMvc.perform(post("/api/profile-conf-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileConfValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfileConfValue in the database
        List<ProfileConfValue> profileConfValueList = profileConfValueRepository.findAll();
        assertThat(profileConfValueList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProfileConfValue in Elasticsearch
        verify(mockProfileConfValueSearchRepository, times(0)).save(profileConfValue);
    }

    @Test
    @Transactional
    public void getAllProfileConfValues() throws Exception {
        // Initialize the database
        profileConfValueRepository.saveAndFlush(profileConfValue);

        // Get all the profileConfValueList
        restProfileConfValueMockMvc.perform(get("/api/profile-conf-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profileConfValue.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getProfileConfValue() throws Exception {
        // Initialize the database
        profileConfValueRepository.saveAndFlush(profileConfValue);

        // Get the profileConfValue
        restProfileConfValueMockMvc.perform(get("/api/profile-conf-values/{id}", profileConfValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profileConfValue.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProfileConfValue() throws Exception {
        // Get the profileConfValue
        restProfileConfValueMockMvc.perform(get("/api/profile-conf-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfileConfValue() throws Exception {
        // Initialize the database
        profileConfValueRepository.saveAndFlush(profileConfValue);

        int databaseSizeBeforeUpdate = profileConfValueRepository.findAll().size();

        // Update the profileConfValue
        ProfileConfValue updatedProfileConfValue = profileConfValueRepository.findById(profileConfValue.getId()).get();
        // Disconnect from session so that the updates on updatedProfileConfValue are not directly saved in db
        em.detach(updatedProfileConfValue);
        ProfileConfValueDTO profileConfValueDTO = profileConfValueMapper.toDto(updatedProfileConfValue);

        restProfileConfValueMockMvc.perform(put("/api/profile-conf-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileConfValueDTO)))
            .andExpect(status().isOk());

        // Validate the ProfileConfValue in the database
        List<ProfileConfValue> profileConfValueList = profileConfValueRepository.findAll();
        assertThat(profileConfValueList).hasSize(databaseSizeBeforeUpdate);
        ProfileConfValue testProfileConfValue = profileConfValueList.get(profileConfValueList.size() - 1);

        // Validate the ProfileConfValue in Elasticsearch
        verify(mockProfileConfValueSearchRepository, times(1)).save(testProfileConfValue);
    }

    @Test
    @Transactional
    public void updateNonExistingProfileConfValue() throws Exception {
        int databaseSizeBeforeUpdate = profileConfValueRepository.findAll().size();

        // Create the ProfileConfValue
        ProfileConfValueDTO profileConfValueDTO = profileConfValueMapper.toDto(profileConfValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileConfValueMockMvc.perform(put("/api/profile-conf-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileConfValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfileConfValue in the database
        List<ProfileConfValue> profileConfValueList = profileConfValueRepository.findAll();
        assertThat(profileConfValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProfileConfValue in Elasticsearch
        verify(mockProfileConfValueSearchRepository, times(0)).save(profileConfValue);
    }

    @Test
    @Transactional
    public void deleteProfileConfValue() throws Exception {
        // Initialize the database
        profileConfValueRepository.saveAndFlush(profileConfValue);

        int databaseSizeBeforeDelete = profileConfValueRepository.findAll().size();

        // Get the profileConfValue
        restProfileConfValueMockMvc.perform(delete("/api/profile-conf-values/{id}", profileConfValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProfileConfValue> profileConfValueList = profileConfValueRepository.findAll();
        assertThat(profileConfValueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProfileConfValue in Elasticsearch
        verify(mockProfileConfValueSearchRepository, times(1)).deleteById(profileConfValue.getId());
    }

    @Test
    @Transactional
    public void searchProfileConfValue() throws Exception {
        // Initialize the database
        profileConfValueRepository.saveAndFlush(profileConfValue);
        when(mockProfileConfValueSearchRepository.search(queryStringQuery("id:" + profileConfValue.getId())))
            .thenReturn(Collections.singletonList(profileConfValue));
        // Search the profileConfValue
        restProfileConfValueMockMvc.perform(get("/api/_search/profile-conf-values?query=id:" + profileConfValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profileConfValue.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileConfValue.class);
        ProfileConfValue profileConfValue1 = new ProfileConfValue();
        profileConfValue1.setId(1L);
        ProfileConfValue profileConfValue2 = new ProfileConfValue();
        profileConfValue2.setId(profileConfValue1.getId());
        assertThat(profileConfValue1).isEqualTo(profileConfValue2);
        profileConfValue2.setId(2L);
        assertThat(profileConfValue1).isNotEqualTo(profileConfValue2);
        profileConfValue1.setId(null);
        assertThat(profileConfValue1).isNotEqualTo(profileConfValue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileConfValueDTO.class);
        ProfileConfValueDTO profileConfValueDTO1 = new ProfileConfValueDTO();
        profileConfValueDTO1.setId(1L);
        ProfileConfValueDTO profileConfValueDTO2 = new ProfileConfValueDTO();
        assertThat(profileConfValueDTO1).isNotEqualTo(profileConfValueDTO2);
        profileConfValueDTO2.setId(profileConfValueDTO1.getId());
        assertThat(profileConfValueDTO1).isEqualTo(profileConfValueDTO2);
        profileConfValueDTO2.setId(2L);
        assertThat(profileConfValueDTO1).isNotEqualTo(profileConfValueDTO2);
        profileConfValueDTO1.setId(null);
        assertThat(profileConfValueDTO1).isNotEqualTo(profileConfValueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(profileConfValueMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(profileConfValueMapper.fromId(null)).isNull();
    }
}
