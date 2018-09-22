package com.uga.gastorama.web.rest;

import com.uga.gastorama.GastoramaApp;

import com.uga.gastorama.domain.CommandLine;
import com.uga.gastorama.repository.CommandLineRepository;
import com.uga.gastorama.repository.search.CommandLineSearchRepository;
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
 * Test class for the CommandLineResource REST controller.
 *
 * @see CommandLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GastoramaApp.class)
public class CommandLineResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private CommandLineRepository commandLineRepository;

    /**
     * This repository is mocked in the com.uga.gastorama.repository.search test package.
     *
     * @see com.uga.gastorama.repository.search.CommandLineSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommandLineSearchRepository mockCommandLineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommandLineMockMvc;

    private CommandLine commandLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommandLineResource commandLineResource = new CommandLineResource(commandLineRepository, mockCommandLineSearchRepository);
        this.restCommandLineMockMvc = MockMvcBuilders.standaloneSetup(commandLineResource)
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
    public static CommandLine createEntity(EntityManager em) {
        CommandLine commandLine = new CommandLine()
            .quantity(DEFAULT_QUANTITY);
        return commandLine;
    }

    @Before
    public void initTest() {
        commandLine = createEntity(em);
    }

    /*@Test
    @Transactional
    public void createCommandLine() throws Exception {
        int databaseSizeBeforeCreate = commandLineRepository.findAll().size();

        // Create the CommandLine
        restCommandLineMockMvc.perform(post("/api/command-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandLine)))
            .andExpect(status().isCreated());

        // Validate the CommandLine in the database
        List<CommandLine> commandLineList = commandLineRepository.findAll();
        assertThat(commandLineList).hasSize(databaseSizeBeforeCreate + 1);
        CommandLine testCommandLine = commandLineList.get(commandLineList.size() - 1);
        assertThat(testCommandLine.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the CommandLine in Elasticsearch
        verify(mockCommandLineSearchRepository, times(1)).save(testCommandLine);
    }

    @Test
    @Transactional
    public void createCommandLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commandLineRepository.findAll().size();

        // Create the CommandLine with an existing ID
        commandLine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandLineMockMvc.perform(post("/api/command-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandLine)))
            .andExpect(status().isBadRequest());

        // Validate the CommandLine in the database
        List<CommandLine> commandLineList = commandLineRepository.findAll();
        assertThat(commandLineList).hasSize(databaseSizeBeforeCreate);

        // Validate the CommandLine in Elasticsearch
        verify(mockCommandLineSearchRepository, times(0)).save(commandLine);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandLineRepository.findAll().size();
        // set the field null
        commandLine.setQuantity(null);

        // Create the CommandLine, which fails.

        restCommandLineMockMvc.perform(post("/api/command-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandLine)))
            .andExpect(status().isBadRequest());

        List<CommandLine> commandLineList = commandLineRepository.findAll();
        assertThat(commandLineList).hasSize(databaseSizeBeforeTest);
    }*/

    @Test
    @Transactional
    public void getAllCommandLines() throws Exception {
        // Initialize the database
        commandLineRepository.saveAndFlush(commandLine);

        // Get all the commandLineList
        restCommandLineMockMvc.perform(get("/api/command-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commandLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @Test
    @Transactional
    public void getCommandLine() throws Exception {
        // Initialize the database
        commandLineRepository.saveAndFlush(commandLine);

        // Get the commandLine
        restCommandLineMockMvc.perform(get("/api/command-lines/{id}", commandLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commandLine.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingCommandLine() throws Exception {
        // Get the commandLine
        restCommandLineMockMvc.perform(get("/api/command-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    /*@Test
    @Transactional
    public void updateCommandLine() throws Exception {
        // Initialize the database
        commandLineRepository.saveAndFlush(commandLine);

        int databaseSizeBeforeUpdate = commandLineRepository.findAll().size();

        // Update the commandLine
        CommandLine updatedCommandLine = commandLineRepository.findById(commandLine.getId()).get();
        // Disconnect from session so that the updates on updatedCommandLine are not directly saved in db
        em.detach(updatedCommandLine);
        updatedCommandLine
            .quantity(UPDATED_QUANTITY);

        restCommandLineMockMvc.perform(put("/api/command-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommandLine)))
            .andExpect(status().isOk());

        // Validate the CommandLine in the database
        List<CommandLine> commandLineList = commandLineRepository.findAll();
        assertThat(commandLineList).hasSize(databaseSizeBeforeUpdate);
        CommandLine testCommandLine = commandLineList.get(commandLineList.size() - 1);
        assertThat(testCommandLine.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the CommandLine in Elasticsearch
        verify(mockCommandLineSearchRepository, times(1)).save(testCommandLine);
    }

    @Test
    @Transactional
    public void updateNonExistingCommandLine() throws Exception {
        int databaseSizeBeforeUpdate = commandLineRepository.findAll().size();

        // Create the CommandLine

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandLineMockMvc.perform(put("/api/command-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandLine)))
            .andExpect(status().isBadRequest());

        // Validate the CommandLine in the database
        List<CommandLine> commandLineList = commandLineRepository.findAll();
        assertThat(commandLineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommandLine in Elasticsearch
        verify(mockCommandLineSearchRepository, times(0)).save(commandLine);
    }

    @Test
    @Transactional
    public void deleteCommandLine() throws Exception {
        // Initialize the database
        commandLineRepository.saveAndFlush(commandLine);

        int databaseSizeBeforeDelete = commandLineRepository.findAll().size();

        // Get the commandLine
        restCommandLineMockMvc.perform(delete("/api/command-lines/{id}", commandLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CommandLine> commandLineList = commandLineRepository.findAll();
        assertThat(commandLineList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CommandLine in Elasticsearch
        verify(mockCommandLineSearchRepository, times(1)).deleteById(commandLine.getId());
    }*/

    @Test
    @Transactional
    public void searchCommandLine() throws Exception {
        // Initialize the database
        commandLineRepository.saveAndFlush(commandLine);
        when(mockCommandLineSearchRepository.search(queryStringQuery("id:" + commandLine.getId())))
            .thenReturn(Collections.singletonList(commandLine));
        // Search the commandLine
        restCommandLineMockMvc.perform(get("/api/_search/command-lines?query=id:" + commandLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commandLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommandLine.class);
        CommandLine commandLine1 = new CommandLine();
        commandLine1.setId(1L);
        CommandLine commandLine2 = new CommandLine();
        commandLine2.setId(commandLine1.getId());
        assertThat(commandLine1).isEqualTo(commandLine2);
        commandLine2.setId(2L);
        assertThat(commandLine1).isNotEqualTo(commandLine2);
        commandLine1.setId(null);
        assertThat(commandLine1).isNotEqualTo(commandLine2);
    }
}
