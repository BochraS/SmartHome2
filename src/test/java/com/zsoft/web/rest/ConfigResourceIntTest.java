package com.zsoft.web.rest;

import com.zsoft.CraApp;

import com.zsoft.domain.Config;
import com.zsoft.repository.ConfigRepository;
import com.zsoft.repository.search.ConfigSearchRepository;
import com.zsoft.service.ConfigService;
import com.zsoft.service.dto.ConfigDTO;
import com.zsoft.service.mapper.ConfigMapper;
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
 * Test class for the ConfigResource REST controller.
 *
 * @see ConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CraApp.class)
public class ConfigResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DES = "AAAAAAAAAA";
    private static final String UPDATED_DES = "BBBBBBBBBB";

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ConfigService configService;

    /**
     * This repository is mocked in the com.zsoft.repository.search test package.
     *
     * @see com.zsoft.repository.search.ConfigSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfigSearchRepository mockConfigSearchRepository;

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

    private MockMvc restConfigMockMvc;

    private Config config;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigResource configResource = new ConfigResource(configService);
        this.restConfigMockMvc = MockMvcBuilders.standaloneSetup(configResource)
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
    public static Config createEntity(EntityManager em) {
        Config config = new Config()
            .key(DEFAULT_KEY)
            .description(DEFAULT_DESCRIPTION)
            .des(DEFAULT_DES);
        return config;
    }

    @Before
    public void initTest() {
        config = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfig() throws Exception {
        int databaseSizeBeforeCreate = configRepository.findAll().size();

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);
        restConfigMockMvc.perform(post("/api/configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isCreated());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeCreate + 1);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testConfig.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testConfig.getDes()).isEqualTo(DEFAULT_DES);

        // Validate the Config in Elasticsearch
        verify(mockConfigSearchRepository, times(1)).save(testConfig);
    }

    @Test
    @Transactional
    public void createConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configRepository.findAll().size();

        // Create the Config with an existing ID
        config.setId(1L);
        ConfigDTO configDTO = configMapper.toDto(config);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMockMvc.perform(post("/api/configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeCreate);

        // Validate the Config in Elasticsearch
        verify(mockConfigSearchRepository, times(0)).save(config);
    }

    @Test
    @Transactional
    public void getAllConfigs() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList
        restConfigMockMvc.perform(get("/api/configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(config.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].des").value(hasItem(DEFAULT_DES.toString())));
    }
    
    @Test
    @Transactional
    public void getConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get the config
        restConfigMockMvc.perform(get("/api/configs/{id}", config.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(config.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.des").value(DEFAULT_DES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfig() throws Exception {
        // Get the config
        restConfigMockMvc.perform(get("/api/configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        int databaseSizeBeforeUpdate = configRepository.findAll().size();

        // Update the config
        Config updatedConfig = configRepository.findById(config.getId()).get();
        // Disconnect from session so that the updates on updatedConfig are not directly saved in db
        em.detach(updatedConfig);
        updatedConfig
            .key(UPDATED_KEY)
            .description(UPDATED_DESCRIPTION)
            .des(UPDATED_DES);
        ConfigDTO configDTO = configMapper.toDto(updatedConfig);

        restConfigMockMvc.perform(put("/api/configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isOk());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConfig.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testConfig.getDes()).isEqualTo(UPDATED_DES);

        // Validate the Config in Elasticsearch
        verify(mockConfigSearchRepository, times(1)).save(testConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().size();

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMockMvc.perform(put("/api/configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Config in Elasticsearch
        verify(mockConfigSearchRepository, times(0)).save(config);
    }

    @Test
    @Transactional
    public void deleteConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        int databaseSizeBeforeDelete = configRepository.findAll().size();

        // Get the config
        restConfigMockMvc.perform(delete("/api/configs/{id}", config.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Config in Elasticsearch
        verify(mockConfigSearchRepository, times(1)).deleteById(config.getId());
    }

    @Test
    @Transactional
    public void searchConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);
        when(mockConfigSearchRepository.search(queryStringQuery("id:" + config.getId())))
            .thenReturn(Collections.singletonList(config));
        // Search the config
        restConfigMockMvc.perform(get("/api/_search/configs?query=id:" + config.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(config.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].des").value(hasItem(DEFAULT_DES)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Config.class);
        Config config1 = new Config();
        config1.setId(1L);
        Config config2 = new Config();
        config2.setId(config1.getId());
        assertThat(config1).isEqualTo(config2);
        config2.setId(2L);
        assertThat(config1).isNotEqualTo(config2);
        config1.setId(null);
        assertThat(config1).isNotEqualTo(config2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigDTO.class);
        ConfigDTO configDTO1 = new ConfigDTO();
        configDTO1.setId(1L);
        ConfigDTO configDTO2 = new ConfigDTO();
        assertThat(configDTO1).isNotEqualTo(configDTO2);
        configDTO2.setId(configDTO1.getId());
        assertThat(configDTO1).isEqualTo(configDTO2);
        configDTO2.setId(2L);
        assertThat(configDTO1).isNotEqualTo(configDTO2);
        configDTO1.setId(null);
        assertThat(configDTO1).isNotEqualTo(configDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(configMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(configMapper.fromId(null)).isNull();
    }
}
