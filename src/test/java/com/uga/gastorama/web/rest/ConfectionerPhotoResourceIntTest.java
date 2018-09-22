package com.uga.gastorama.web.rest;

import com.uga.gastorama.GastoramaApp;

import com.uga.gastorama.domain.ConfectionerPhoto;
import com.uga.gastorama.repository.ConfectionerPhotoRepository;
import com.uga.gastorama.repository.search.ConfectionerPhotoSearchRepository;
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
 * Test class for the ConfectionerPhotoResource REST controller.
 *
 * @see ConfectionerPhotoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GastoramaApp.class)
public class ConfectionerPhotoResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private ConfectionerPhotoRepository confectionerPhotoRepository;

    /**
     * This repository is mocked in the com.uga.gastorama.repository.search test package.
     *
     * @see com.uga.gastorama.repository.search.ConfectionerPhotoSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfectionerPhotoSearchRepository mockConfectionerPhotoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConfectionerPhotoMockMvc;

    private ConfectionerPhoto confectionerPhoto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfectionerPhotoResource confectionerPhotoResource = new ConfectionerPhotoResource(confectionerPhotoRepository, mockConfectionerPhotoSearchRepository);
        this.restConfectionerPhotoMockMvc = MockMvcBuilders.standaloneSetup(confectionerPhotoResource)
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
    public static ConfectionerPhoto createEntity(EntityManager em) {
        ConfectionerPhoto confectionerPhoto = new ConfectionerPhoto()
            .url(DEFAULT_URL);
        return confectionerPhoto;
    }

    @Before
    public void initTest() {
        confectionerPhoto = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfectionerPhoto() throws Exception {
        int databaseSizeBeforeCreate = confectionerPhotoRepository.findAll().size();

        // Create the ConfectionerPhoto
        restConfectionerPhotoMockMvc.perform(post("/api/confectioner-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confectionerPhoto)))
            .andExpect(status().isCreated());

        // Validate the ConfectionerPhoto in the database
        List<ConfectionerPhoto> confectionerPhotoList = confectionerPhotoRepository.findAll();
        assertThat(confectionerPhotoList).hasSize(databaseSizeBeforeCreate + 1);
        ConfectionerPhoto testConfectionerPhoto = confectionerPhotoList.get(confectionerPhotoList.size() - 1);
        assertThat(testConfectionerPhoto.getUrl()).isEqualTo(DEFAULT_URL);

        // Validate the ConfectionerPhoto in Elasticsearch
        verify(mockConfectionerPhotoSearchRepository, times(1)).save(testConfectionerPhoto);
    }

    @Test
    @Transactional
    public void createConfectionerPhotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = confectionerPhotoRepository.findAll().size();

        // Create the ConfectionerPhoto with an existing ID
        confectionerPhoto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfectionerPhotoMockMvc.perform(post("/api/confectioner-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confectionerPhoto)))
            .andExpect(status().isBadRequest());

        // Validate the ConfectionerPhoto in the database
        List<ConfectionerPhoto> confectionerPhotoList = confectionerPhotoRepository.findAll();
        assertThat(confectionerPhotoList).hasSize(databaseSizeBeforeCreate);

        // Validate the ConfectionerPhoto in Elasticsearch
        verify(mockConfectionerPhotoSearchRepository, times(0)).save(confectionerPhoto);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = confectionerPhotoRepository.findAll().size();
        // set the field null
        confectionerPhoto.setUrl(null);

        // Create the ConfectionerPhoto, which fails.

        restConfectionerPhotoMockMvc.perform(post("/api/confectioner-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confectionerPhoto)))
            .andExpect(status().isBadRequest());

        List<ConfectionerPhoto> confectionerPhotoList = confectionerPhotoRepository.findAll();
        assertThat(confectionerPhotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfectionerPhotos() throws Exception {
        // Initialize the database
        confectionerPhotoRepository.saveAndFlush(confectionerPhoto);

        // Get all the confectionerPhotoList
        restConfectionerPhotoMockMvc.perform(get("/api/confectioner-photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confectionerPhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getConfectionerPhoto() throws Exception {
        // Initialize the database
        confectionerPhotoRepository.saveAndFlush(confectionerPhoto);

        // Get the confectionerPhoto
        restConfectionerPhotoMockMvc.perform(get("/api/confectioner-photos/{id}", confectionerPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(confectionerPhoto.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfectionerPhoto() throws Exception {
        // Get the confectionerPhoto
        restConfectionerPhotoMockMvc.perform(get("/api/confectioner-photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfectionerPhoto() throws Exception {
        // Initialize the database
        confectionerPhotoRepository.saveAndFlush(confectionerPhoto);

        int databaseSizeBeforeUpdate = confectionerPhotoRepository.findAll().size();

        // Update the confectionerPhoto
        ConfectionerPhoto updatedConfectionerPhoto = confectionerPhotoRepository.findById(confectionerPhoto.getId()).get();
        // Disconnect from session so that the updates on updatedConfectionerPhoto are not directly saved in db
        em.detach(updatedConfectionerPhoto);
        updatedConfectionerPhoto
            .url(UPDATED_URL);

        restConfectionerPhotoMockMvc.perform(put("/api/confectioner-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfectionerPhoto)))
            .andExpect(status().isOk());

        // Validate the ConfectionerPhoto in the database
        List<ConfectionerPhoto> confectionerPhotoList = confectionerPhotoRepository.findAll();
        assertThat(confectionerPhotoList).hasSize(databaseSizeBeforeUpdate);
        ConfectionerPhoto testConfectionerPhoto = confectionerPhotoList.get(confectionerPhotoList.size() - 1);
        assertThat(testConfectionerPhoto.getUrl()).isEqualTo(UPDATED_URL);

        // Validate the ConfectionerPhoto in Elasticsearch
        verify(mockConfectionerPhotoSearchRepository, times(1)).save(testConfectionerPhoto);
    }

    @Test
    @Transactional
    public void updateNonExistingConfectionerPhoto() throws Exception {
        int databaseSizeBeforeUpdate = confectionerPhotoRepository.findAll().size();

        // Create the ConfectionerPhoto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfectionerPhotoMockMvc.perform(put("/api/confectioner-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confectionerPhoto)))
            .andExpect(status().isBadRequest());

        // Validate the ConfectionerPhoto in the database
        List<ConfectionerPhoto> confectionerPhotoList = confectionerPhotoRepository.findAll();
        assertThat(confectionerPhotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ConfectionerPhoto in Elasticsearch
        verify(mockConfectionerPhotoSearchRepository, times(0)).save(confectionerPhoto);
    }

    @Test
    @Transactional
    public void deleteConfectionerPhoto() throws Exception {
        // Initialize the database
        confectionerPhotoRepository.saveAndFlush(confectionerPhoto);

        int databaseSizeBeforeDelete = confectionerPhotoRepository.findAll().size();

        // Get the confectionerPhoto
        restConfectionerPhotoMockMvc.perform(delete("/api/confectioner-photos/{id}", confectionerPhoto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ConfectionerPhoto> confectionerPhotoList = confectionerPhotoRepository.findAll();
        assertThat(confectionerPhotoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ConfectionerPhoto in Elasticsearch
        verify(mockConfectionerPhotoSearchRepository, times(1)).deleteById(confectionerPhoto.getId());
    }

    @Test
    @Transactional
    public void searchConfectionerPhoto() throws Exception {
        // Initialize the database
        confectionerPhotoRepository.saveAndFlush(confectionerPhoto);
        when(mockConfectionerPhotoSearchRepository.search(queryStringQuery("id:" + confectionerPhoto.getId())))
            .thenReturn(Collections.singletonList(confectionerPhoto));
        // Search the confectionerPhoto
        restConfectionerPhotoMockMvc.perform(get("/api/_search/confectioner-photos?query=id:" + confectionerPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confectionerPhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfectionerPhoto.class);
        ConfectionerPhoto confectionerPhoto1 = new ConfectionerPhoto();
        confectionerPhoto1.setId(1L);
        ConfectionerPhoto confectionerPhoto2 = new ConfectionerPhoto();
        confectionerPhoto2.setId(confectionerPhoto1.getId());
        assertThat(confectionerPhoto1).isEqualTo(confectionerPhoto2);
        confectionerPhoto2.setId(2L);
        assertThat(confectionerPhoto1).isNotEqualTo(confectionerPhoto2);
        confectionerPhoto1.setId(null);
        assertThat(confectionerPhoto1).isNotEqualTo(confectionerPhoto2);
    }
}
