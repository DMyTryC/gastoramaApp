package com.uga.gastorama.web.rest;

import com.uga.gastorama.GastoramaApp;

import com.uga.gastorama.domain.Confectioner;
import com.uga.gastorama.repository.ConfectionerRepository;
import com.uga.gastorama.repository.search.ConfectionerSearchRepository;
import com.uga.gastorama.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.uga.gastorama.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ConfectionerResource REST controller.
 *
 * @see ConfectionerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GastoramaApp.class)
public class ConfectionerResourceIntTest {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ConfectionerRepository confectionerRepository;

    /**
     * This repository is mocked in the com.uga.gastorama.repository.search test package.
     *
     * @see com.uga.gastorama.repository.search.ConfectionerSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfectionerSearchRepository mockConfectionerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConfectionerMockMvc;

    private Confectioner confectioner;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfectionerResource confectionerResource = new ConfectionerResource(confectionerRepository, mockConfectionerSearchRepository);
        this.restConfectionerMockMvc = MockMvcBuilders.standaloneSetup(confectionerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Confectioner createEntity(EntityManager em) {
        Confectioner confectioner = new Confectioner()
            .address(DEFAULT_ADDRESS)
            .description(DEFAULT_DESCRIPTION);
        return confectioner;
    }

    @Before
    public void initTest() {
        confectioner = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfectioner() throws Exception {
        int databaseSizeBeforeCreate = confectionerRepository.findAll().size();

        // Create the Confectioner
        restConfectionerMockMvc.perform(post("/api/confectioners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confectioner)))
            .andExpect(status().isCreated());

        // Validate the Confectioner in the database
        List<Confectioner> confectionerList = confectionerRepository.findAll();
        assertThat(confectionerList).hasSize(databaseSizeBeforeCreate + 1);
        Confectioner testConfectioner = confectionerList.get(confectionerList.size() - 1);
        assertThat(testConfectioner.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testConfectioner.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Confectioner in Elasticsearch
        verify(mockConfectionerSearchRepository, times(1)).save(testConfectioner);
    }

    @Test
    @Transactional
    public void createConfectionerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = confectionerRepository.findAll().size();

        // Create the Confectioner with an existing ID
        confectioner.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfectionerMockMvc.perform(post("/api/confectioners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confectioner)))
            .andExpect(status().isBadRequest());

        // Validate the Confectioner in the database
        List<Confectioner> confectionerList = confectionerRepository.findAll();
        assertThat(confectionerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Confectioner in Elasticsearch
        verify(mockConfectionerSearchRepository, times(0)).save(confectioner);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = confectionerRepository.findAll().size();
        // set the field null
        confectioner.setAddress(null);

        // Create the Confectioner, which fails.

        restConfectionerMockMvc.perform(post("/api/confectioners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confectioner)))
            .andExpect(status().isBadRequest());

        List<Confectioner> confectionerList = confectionerRepository.findAll();
        assertThat(confectionerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfectioners() throws Exception {
        // Initialize the database
        confectionerRepository.saveAndFlush(confectioner);

        // Get all the confectionerList
        restConfectionerMockMvc.perform(get("/api/confectioners?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confectioner.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getConfectioner() throws Exception {
        // Initialize the database
        confectionerRepository.saveAndFlush(confectioner);

        // Get the confectioner
        restConfectionerMockMvc.perform(get("/api/confectioners/{id}", confectioner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(confectioner.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfectioner() throws Exception {
        // Get the confectioner
        restConfectionerMockMvc.perform(get("/api/confectioners/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfectioner() throws Exception {
        // Initialize the database
        confectionerRepository.saveAndFlush(confectioner);

        int databaseSizeBeforeUpdate = confectionerRepository.findAll().size();

        // Update the confectioner
        Confectioner updatedConfectioner = confectionerRepository.findById(confectioner.getId()).get();
        // Disconnect from session so that the updates on updatedConfectioner are not directly saved in db
        em.detach(updatedConfectioner);
        updatedConfectioner
            .address(UPDATED_ADDRESS)
            .description(UPDATED_DESCRIPTION);

        restConfectionerMockMvc.perform(put("/api/confectioners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfectioner)))
            .andExpect(status().isOk());

        // Validate the Confectioner in the database
        List<Confectioner> confectionerList = confectionerRepository.findAll();
        assertThat(confectionerList).hasSize(databaseSizeBeforeUpdate);
        Confectioner testConfectioner = confectionerList.get(confectionerList.size() - 1);
        assertThat(testConfectioner.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testConfectioner.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Confectioner in Elasticsearch
        verify(mockConfectionerSearchRepository, times(1)).save(testConfectioner);
    }

    @Test
    @Transactional
    public void updateNonExistingConfectioner() throws Exception {
        int databaseSizeBeforeUpdate = confectionerRepository.findAll().size();

        // Create the Confectioner

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfectionerMockMvc.perform(put("/api/confectioners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confectioner)))
            .andExpect(status().isBadRequest());

        // Validate the Confectioner in the database
        List<Confectioner> confectionerList = confectionerRepository.findAll();
        assertThat(confectionerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Confectioner in Elasticsearch
        verify(mockConfectionerSearchRepository, times(0)).save(confectioner);
    }

    @Test
    @Transactional
    public void deleteConfectioner() throws Exception {
        // Initialize the database
        confectionerRepository.saveAndFlush(confectioner);

        int databaseSizeBeforeDelete = confectionerRepository.findAll().size();

        // Get the confectioner
        restConfectionerMockMvc.perform(delete("/api/confectioners/{id}", confectioner.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Confectioner> confectionerList = confectionerRepository.findAll();
        assertThat(confectionerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Confectioner in Elasticsearch
        verify(mockConfectionerSearchRepository, times(1)).deleteById(confectioner.getId());
    }

    @Test
    @Transactional
    public void searchConfectioner() throws Exception {
        // Initialize the database
        confectionerRepository.saveAndFlush(confectioner);
        when(mockConfectionerSearchRepository.search(queryStringQuery("id:" + confectioner.getId())))
            .thenReturn(Collections.singletonList(confectioner));
        // Search the confectioner
        restConfectionerMockMvc.perform(get("/api/_search/confectioners?query=id:" + confectioner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confectioner.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Confectioner.class);
        Confectioner confectioner1 = new Confectioner();
        confectioner1.setId(1L);
        Confectioner confectioner2 = new Confectioner();
        confectioner2.setId(confectioner1.getId());
        assertThat(confectioner1).isEqualTo(confectioner2);
        confectioner2.setId(2L);
        assertThat(confectioner1).isNotEqualTo(confectioner2);
        confectioner1.setId(null);
        assertThat(confectioner1).isNotEqualTo(confectioner2);
    }
}
