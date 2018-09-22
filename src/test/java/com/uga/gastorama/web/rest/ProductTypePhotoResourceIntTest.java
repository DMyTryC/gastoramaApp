package com.uga.gastorama.web.rest;

import com.uga.gastorama.GastoramaApp;

import com.uga.gastorama.domain.ProductTypePhoto;
import com.uga.gastorama.repository.ProductTypePhotoRepository;
import com.uga.gastorama.repository.search.ProductTypePhotoSearchRepository;
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
 * Test class for the ProductTypePhotoResource REST controller.
 *
 * @see ProductTypePhotoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GastoramaApp.class)
public class ProductTypePhotoResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private ProductTypePhotoRepository productTypePhotoRepository;

    /**
     * This repository is mocked in the com.uga.gastorama.repository.search test package.
     *
     * @see com.uga.gastorama.repository.search.ProductTypePhotoSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProductTypePhotoSearchRepository mockProductTypePhotoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductTypePhotoMockMvc;

    private ProductTypePhoto productTypePhoto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductTypePhotoResource productTypePhotoResource = new ProductTypePhotoResource(productTypePhotoRepository, mockProductTypePhotoSearchRepository);
        this.restProductTypePhotoMockMvc = MockMvcBuilders.standaloneSetup(productTypePhotoResource)
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
    public static ProductTypePhoto createEntity(EntityManager em) {
        ProductTypePhoto productTypePhoto = new ProductTypePhoto()
            .url(DEFAULT_URL);
        return productTypePhoto;
    }

    @Before
    public void initTest() {
        productTypePhoto = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductTypePhoto() throws Exception {
        int databaseSizeBeforeCreate = productTypePhotoRepository.findAll().size();

        // Create the ProductTypePhoto
        restProductTypePhotoMockMvc.perform(post("/api/product-type-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productTypePhoto)))
            .andExpect(status().isCreated());

        // Validate the ProductTypePhoto in the database
        List<ProductTypePhoto> productTypePhotoList = productTypePhotoRepository.findAll();
        assertThat(productTypePhotoList).hasSize(databaseSizeBeforeCreate + 1);
        ProductTypePhoto testProductTypePhoto = productTypePhotoList.get(productTypePhotoList.size() - 1);
        assertThat(testProductTypePhoto.getUrl()).isEqualTo(DEFAULT_URL);

        // Validate the ProductTypePhoto in Elasticsearch
        verify(mockProductTypePhotoSearchRepository, times(1)).save(testProductTypePhoto);
    }

    @Test
    @Transactional
    public void createProductTypePhotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productTypePhotoRepository.findAll().size();

        // Create the ProductTypePhoto with an existing ID
        productTypePhoto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductTypePhotoMockMvc.perform(post("/api/product-type-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productTypePhoto)))
            .andExpect(status().isBadRequest());

        // Validate the ProductTypePhoto in the database
        List<ProductTypePhoto> productTypePhotoList = productTypePhotoRepository.findAll();
        assertThat(productTypePhotoList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProductTypePhoto in Elasticsearch
        verify(mockProductTypePhotoSearchRepository, times(0)).save(productTypePhoto);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTypePhotoRepository.findAll().size();
        // set the field null
        productTypePhoto.setUrl(null);

        // Create the ProductTypePhoto, which fails.

        restProductTypePhotoMockMvc.perform(post("/api/product-type-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productTypePhoto)))
            .andExpect(status().isBadRequest());

        List<ProductTypePhoto> productTypePhotoList = productTypePhotoRepository.findAll();
        assertThat(productTypePhotoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductTypePhotos() throws Exception {
        // Initialize the database
        productTypePhotoRepository.saveAndFlush(productTypePhoto);

        // Get all the productTypePhotoList
        restProductTypePhotoMockMvc.perform(get("/api/product-type-photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productTypePhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getProductTypePhoto() throws Exception {
        // Initialize the database
        productTypePhotoRepository.saveAndFlush(productTypePhoto);

        // Get the productTypePhoto
        restProductTypePhotoMockMvc.perform(get("/api/product-type-photos/{id}", productTypePhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productTypePhoto.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductTypePhoto() throws Exception {
        // Get the productTypePhoto
        restProductTypePhotoMockMvc.perform(get("/api/product-type-photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductTypePhoto() throws Exception {
        // Initialize the database
        productTypePhotoRepository.saveAndFlush(productTypePhoto);

        int databaseSizeBeforeUpdate = productTypePhotoRepository.findAll().size();

        // Update the productTypePhoto
        ProductTypePhoto updatedProductTypePhoto = productTypePhotoRepository.findById(productTypePhoto.getId()).get();
        // Disconnect from session so that the updates on updatedProductTypePhoto are not directly saved in db
        em.detach(updatedProductTypePhoto);
        updatedProductTypePhoto
            .url(UPDATED_URL);

        restProductTypePhotoMockMvc.perform(put("/api/product-type-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductTypePhoto)))
            .andExpect(status().isOk());

        // Validate the ProductTypePhoto in the database
        List<ProductTypePhoto> productTypePhotoList = productTypePhotoRepository.findAll();
        assertThat(productTypePhotoList).hasSize(databaseSizeBeforeUpdate);
        ProductTypePhoto testProductTypePhoto = productTypePhotoList.get(productTypePhotoList.size() - 1);
        assertThat(testProductTypePhoto.getUrl()).isEqualTo(UPDATED_URL);

        // Validate the ProductTypePhoto in Elasticsearch
        verify(mockProductTypePhotoSearchRepository, times(1)).save(testProductTypePhoto);
    }

    @Test
    @Transactional
    public void updateNonExistingProductTypePhoto() throws Exception {
        int databaseSizeBeforeUpdate = productTypePhotoRepository.findAll().size();

        // Create the ProductTypePhoto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTypePhotoMockMvc.perform(put("/api/product-type-photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productTypePhoto)))
            .andExpect(status().isBadRequest());

        // Validate the ProductTypePhoto in the database
        List<ProductTypePhoto> productTypePhotoList = productTypePhotoRepository.findAll();
        assertThat(productTypePhotoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProductTypePhoto in Elasticsearch
        verify(mockProductTypePhotoSearchRepository, times(0)).save(productTypePhoto);
    }

    @Test
    @Transactional
    public void deleteProductTypePhoto() throws Exception {
        // Initialize the database
        productTypePhotoRepository.saveAndFlush(productTypePhoto);

        int databaseSizeBeforeDelete = productTypePhotoRepository.findAll().size();

        // Get the productTypePhoto
        restProductTypePhotoMockMvc.perform(delete("/api/product-type-photos/{id}", productTypePhoto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductTypePhoto> productTypePhotoList = productTypePhotoRepository.findAll();
        assertThat(productTypePhotoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProductTypePhoto in Elasticsearch
        verify(mockProductTypePhotoSearchRepository, times(1)).deleteById(productTypePhoto.getId());
    }

    @Test
    @Transactional
    public void searchProductTypePhoto() throws Exception {
        // Initialize the database
        productTypePhotoRepository.saveAndFlush(productTypePhoto);
        when(mockProductTypePhotoSearchRepository.search(queryStringQuery("id:" + productTypePhoto.getId())))
            .thenReturn(Collections.singletonList(productTypePhoto));
        // Search the productTypePhoto
        restProductTypePhotoMockMvc.perform(get("/api/_search/product-type-photos?query=id:" + productTypePhoto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productTypePhoto.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductTypePhoto.class);
        ProductTypePhoto productTypePhoto1 = new ProductTypePhoto();
        productTypePhoto1.setId(1L);
        ProductTypePhoto productTypePhoto2 = new ProductTypePhoto();
        productTypePhoto2.setId(productTypePhoto1.getId());
        assertThat(productTypePhoto1).isEqualTo(productTypePhoto2);
        productTypePhoto2.setId(2L);
        assertThat(productTypePhoto1).isNotEqualTo(productTypePhoto2);
        productTypePhoto1.setId(null);
        assertThat(productTypePhoto1).isNotEqualTo(productTypePhoto2);
    }
}
