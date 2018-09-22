package com.uga.gastorama.web.rest;

import com.uga.gastorama.GastoramaApp;

import com.uga.gastorama.domain.ProductPhoto;
import com.uga.gastorama.repository.ProductPhotoRepository;
import com.uga.gastorama.repository.search.ProductPhotoSearchRepository;
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
 * Test class for the ProductPhotoResource REST controller.
 *
 * @see ProductPhotoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GastoramaApp.class)
public class ProductPhotoResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private ProductPhotoRepository productPhotoRepository;

    /**
     * This repository is mocked in the com.uga.gastorama.repository.search test package.
     *
     * @see com.uga.gastorama.repository.search.ProductPhotoSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductPhotoSearchRepository mockProductPhotoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductPhotoMockMvc;

    private ProductPhoto productPhoto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductPhotoResource productPhotoResource = new ProductPhotoResource(productPhotoRepository, mockProductPhotoSearchRepository);
        this.restProductPhotoMockMvc = MockMvcBuilders.standaloneSetup(productPhotoResource)
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
    public static ProductPhoto createEntity(EntityManager em) {
        ProductPhoto productPhoto = new ProductPhoto()
            .url(DEFAULT_URL);
        return productPhoto;
    }

    @Before
    public void initTest() {
        productPhoto = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductPhoto() throws Exception {
        int databaseSizeBeforeCreate = productPhotoRepository.findAll().size();

        // Create the ProductPhoto
        restProductPhotoMockMvc.perform(post("/api/product-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productPhoto)))
            .andExpect(status().isCreated());

        // Validate the ProductPhoto in the database
        List<ProductPhoto> productPhotoList = productPhotoRepository.findAll();
        assertThat(productPhotoList).hasSize(databaseSizeBeforeCreate + 1);
        ProductPhoto testProductPhoto = productPhotoList.get(productPhotoList.size() - 1);
        assertThat(testProductPhoto.getUrl()).isEqualTo(DEFAULT_URL);

        // Validate the ProductPhoto in Elasticsearch
        verify(mockProductPhotoSearchRepository, times(1)).save(testProductPhoto);
    }

    @Test
    @Transactional
    public void createProductPhotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productPhotoRepository.findAll().size();

        // Create the ProductPhoto with an existing ID
        productPhoto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductPhotoMockMvc.perform(post("/api/product-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productPhoto)))
            .andExpect(status().isBadRequest());

        // Validate the ProductPhoto in the database
        List<ProductPhoto> productPhotoList = productPhotoRepository.findAll();
        assertThat(productPhotoList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProductPhoto in Elasticsearch
        verify(mockProductPhotoSearchRepository, times(0)).save(productPhoto);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = productPhotoRepository.findAll().size();
        // set the field null
        productPhoto.setUrl(null);

        // Create the ProductPhoto, which fails.

        restProductPhotoMockMvc.perform(post("/api/product-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productPhoto)))
            .andExpect(status().isBadRequest());

        List<ProductPhoto> productPhotoList = productPhotoRepository.findAll();
        assertThat(productPhotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductPhotos() throws Exception {
        // Initialize the database
        productPhotoRepository.saveAndFlush(productPhoto);

        // Get all the productPhotoList
        restProductPhotoMockMvc.perform(get("/api/product-photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getProductPhoto() throws Exception {
        // Initialize the database
        productPhotoRepository.saveAndFlush(productPhoto);

        // Get the productPhoto
        restProductPhotoMockMvc.perform(get("/api/product-photos/{id}", productPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productPhoto.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductPhoto() throws Exception {
        // Get the productPhoto
        restProductPhotoMockMvc.perform(get("/api/product-photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductPhoto() throws Exception {
        // Initialize the database
        productPhotoRepository.saveAndFlush(productPhoto);

        int databaseSizeBeforeUpdate = productPhotoRepository.findAll().size();

        // Update the productPhoto
        ProductPhoto updatedProductPhoto = productPhotoRepository.findById(productPhoto.getId()).get();
        // Disconnect from session so that the updates on updatedProductPhoto are not directly saved in db
        em.detach(updatedProductPhoto);
        updatedProductPhoto
            .url(UPDATED_URL);

        restProductPhotoMockMvc.perform(put("/api/product-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductPhoto)))
            .andExpect(status().isOk());

        // Validate the ProductPhoto in the database
        List<ProductPhoto> productPhotoList = productPhotoRepository.findAll();
        assertThat(productPhotoList).hasSize(databaseSizeBeforeUpdate);
        ProductPhoto testProductPhoto = productPhotoList.get(productPhotoList.size() - 1);
        assertThat(testProductPhoto.getUrl()).isEqualTo(UPDATED_URL);

        // Validate the ProductPhoto in Elasticsearch
        verify(mockProductPhotoSearchRepository, times(1)).save(testProductPhoto);
    }

    @Test
    @Transactional
    public void updateNonExistingProductPhoto() throws Exception {
        int databaseSizeBeforeUpdate = productPhotoRepository.findAll().size();

        // Create the ProductPhoto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPhotoMockMvc.perform(put("/api/product-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productPhoto)))
            .andExpect(status().isBadRequest());

        // Validate the ProductPhoto in the database
        List<ProductPhoto> productPhotoList = productPhotoRepository.findAll();
        assertThat(productPhotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductPhoto in Elasticsearch
        verify(mockProductPhotoSearchRepository, times(0)).save(productPhoto);
    }

    @Test
    @Transactional
    public void deleteProductPhoto() throws Exception {
        // Initialize the database
        productPhotoRepository.saveAndFlush(productPhoto);

        int databaseSizeBeforeDelete = productPhotoRepository.findAll().size();

        // Get the productPhoto
        restProductPhotoMockMvc.perform(delete("/api/product-photos/{id}", productPhoto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductPhoto> productPhotoList = productPhotoRepository.findAll();
        assertThat(productPhotoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProductPhoto in Elasticsearch
        verify(mockProductPhotoSearchRepository, times(1)).deleteById(productPhoto.getId());
    }

    @Test
    @Transactional
    public void searchProductPhoto() throws Exception {
        // Initialize the database
        productPhotoRepository.saveAndFlush(productPhoto);
        when(mockProductPhotoSearchRepository.search(queryStringQuery("id:" + productPhoto.getId())))
            .thenReturn(Collections.singletonList(productPhoto));
        // Search the productPhoto
        restProductPhotoMockMvc.perform(get("/api/_search/product-photos?query=id:" + productPhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductPhoto.class);
        ProductPhoto productPhoto1 = new ProductPhoto();
        productPhoto1.setId(1L);
        ProductPhoto productPhoto2 = new ProductPhoto();
        productPhoto2.setId(productPhoto1.getId());
        assertThat(productPhoto1).isEqualTo(productPhoto2);
        productPhoto2.setId(2L);
        assertThat(productPhoto1).isNotEqualTo(productPhoto2);
        productPhoto1.setId(null);
        assertThat(productPhoto1).isNotEqualTo(productPhoto2);
    }
}
