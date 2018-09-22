package com.uga.gastorama.web.rest;

import com.uga.gastorama.GastoramaApp;

import com.uga.gastorama.domain.Command;
import com.uga.gastorama.domain.CommandLine;
import com.uga.gastorama.domain.Product;
import com.uga.gastorama.domain.User;
import com.uga.gastorama.repository.CommandRepository;
import com.uga.gastorama.repository.ProductRepository;
import com.uga.gastorama.repository.UserRepository;
import com.uga.gastorama.repository.search.CommandLineSearchRepository;
import com.uga.gastorama.repository.search.CommandSearchRepository;
import com.uga.gastorama.service.CommandService;
import com.uga.gastorama.service.UserService;
import com.uga.gastorama.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


import static com.uga.gastorama.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uga.gastorama.domain.enumeration.DeliveryMode;
import com.uga.gastorama.domain.enumeration.State;
/**
 * Test class for the CommandResource REST controller.
 *
 * @see CommandResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GastoramaApp.class)
public class CommandResourceIntTest {

    private static final Integer DEFAULT_SUMPRICE = 0;
    private static final Integer UPDATED_SUMPRICE = 2;

    private static final String DEFAULT_DELIVERY_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_ADDRESS = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DeliveryMode DEFAULT_DELIVERY = DeliveryMode.DOMICILE;
    private static final DeliveryMode UPDATED_DELIVERY = DeliveryMode.PATISSERIE;

    private static final State DEFAULT_STATE = State.VALIDATED;
    private static final State UPDATED_STATE = State.READY_TO_GO;

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommandService commandService;

    /**
     * This repository is mocked in the com.uga.gastorama.repository.search test package.
     *
     * @see com.uga.gastorama.repository.search.CommandSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommandSearchRepository mockCommandSearchRepository;

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

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private MockMvc restCommandMockMvc;

    private Command command;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommandResource commandResource = new CommandResource(commandRepository, mockCommandSearchRepository, mockCommandLineSearchRepository, commandService);
        this.restCommandMockMvc = MockMvcBuilders.standaloneSetup(commandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Transactional
    public Command createEntity(EntityManager em) {
        Command command = new Command()
            .sumprice(DEFAULT_SUMPRICE)
            .deliveryAddress(DEFAULT_DELIVERY_ADDRESS)
            .date(DEFAULT_DATE)
            .delivery(DEFAULT_DELIVERY)
            .state(DEFAULT_STATE);
        Set<CommandLine> commandLineSet = new HashSet<CommandLine>();

        CommandLine commandLine = new CommandLine();

        Product product;

        product = new Product();
        product.setId((long) 7);
        product.setStock(10);

        commandLine.setProduct(product);
        commandLine.setQuantity(1);

        commandLineSet.add(commandLine);

        //commandLine.setCommand(command);
        command.setCommandLines(commandLineSet);

        User user;

        user = new User();

        user.setId((long) 1);

        command.setUserId(user);

        return command;
    }

    @Before
    public void initTest() {
        command = createEntity(em);
    }

    /*@Test
    @Transactional
    public void createCommand() throws Exception {
        int databaseSizeBeforeCreate = commandRepository.findAll().size();

        // Create the Command
        ResultActions resAction = restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(command)));

        resAction.andExpect(status().isCreated());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate + 1);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getSumprice()).isEqualTo(DEFAULT_SUMPRICE);
        assertThat(testCommand.getDeliveryAddress()).isEqualTo(DEFAULT_DELIVERY_ADDRESS);
        assertThat(testCommand.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCommand.getDelivery()).isEqualTo(DEFAULT_DELIVERY);
        assertThat(testCommand.getState()).isEqualTo(DEFAULT_STATE);

        // Validate the Command in Elasticsearch
        verify(mockCommandSearchRepository, times(1)).save(testCommand);
    }*/

    @Test
    @Transactional
    public void createCommandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commandRepository.findAll().size();

        // Create the Command with an existing ID
        command.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(command)))
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate);

        // Validate the Command in Elasticsearch
        verify(mockCommandSearchRepository, times(0)).save(command);
    }

    @Test
    @Transactional
    public void checkSumpriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandRepository.findAll().size();
        // set the field null
        command.setSumprice(null);

        // Create the Command, which fails.

        restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(command)))
            .andExpect(status().isBadRequest());

        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeliveryAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandRepository.findAll().size();
        // set the field null
        command.setDeliveryAddress(null);

        // Create the Command, which fails.

        restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(command)))
            .andExpect(status().isBadRequest());

        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandRepository.findAll().size();
        // set the field null
        command.setDate(null);

        // Create the Command, which fails.

        restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(command)))
            .andExpect(status().isBadRequest());

        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeliveryIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandRepository.findAll().size();
        // set the field null
        command.setDelivery(null);

        // Create the Command, which fails.

        restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(command)))
            .andExpect(status().isBadRequest());

        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandRepository.findAll().size();
        // set the field null
        command.setState(null);

        // Create the Command, which fails.

        restCommandMockMvc.perform(post("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(command)))
            .andExpect(status().isBadRequest());

        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommands() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList
        restCommandMockMvc.perform(get("/api/commands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(command.getId().intValue())))
            .andExpect(jsonPath("$.[*].sumprice").value(hasItem(DEFAULT_SUMPRICE)))
            .andExpect(jsonPath("$.[*].deliveryAddress").value(hasItem(DEFAULT_DELIVERY_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].delivery").value(hasItem(DEFAULT_DELIVERY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get the command
        restCommandMockMvc.perform(get("/api/commands/{id}", command.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(command.getId().intValue()))
            .andExpect(jsonPath("$.sumprice").value(DEFAULT_SUMPRICE))
            .andExpect(jsonPath("$.deliveryAddress").value(DEFAULT_DELIVERY_ADDRESS.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.delivery").value(DEFAULT_DELIVERY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommand() throws Exception {
        // Get the command
        restCommandMockMvc.perform(get("/api/commands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Update the command
        Command updatedCommand = commandRepository.findById(command.getId()).get();
        // Disconnect from session so that the updates on updatedCommand are not directly saved in db
        em.detach(updatedCommand);
        updatedCommand
            .sumprice(UPDATED_SUMPRICE)
            .deliveryAddress(UPDATED_DELIVERY_ADDRESS)
            .date(UPDATED_DATE)
            .delivery(UPDATED_DELIVERY)
            .state(UPDATED_STATE);

        restCommandMockMvc.perform(put("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommand)))
            .andExpect(status().isOk());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getSumprice()).isEqualTo(UPDATED_SUMPRICE);
        assertThat(testCommand.getDeliveryAddress()).isEqualTo(UPDATED_DELIVERY_ADDRESS);
        assertThat(testCommand.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCommand.getDelivery()).isEqualTo(UPDATED_DELIVERY);
        assertThat(testCommand.getState()).isEqualTo(UPDATED_STATE);

        // Validate the Command in Elasticsearch
        verify(mockCommandSearchRepository, times(1)).save(testCommand);
    }

    @Test
    @Transactional
    public void updateNonExistingCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Create the Command

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandMockMvc.perform(put("/api/commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(command)))
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Command in Elasticsearch
        verify(mockCommandSearchRepository, times(0)).save(command);
    }

    @Test
    @Transactional
    public void deleteCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        int databaseSizeBeforeDelete = commandRepository.findAll().size();

        // Get the command
        restCommandMockMvc.perform(delete("/api/commands/{id}", command.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Command in Elasticsearch
        verify(mockCommandSearchRepository, times(1)).deleteById(command.getId());
    }

    @Test
    @Transactional
    public void searchCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);
        when(mockCommandSearchRepository.search(queryStringQuery("id:" + command.getId())))
            .thenReturn(Collections.singletonList(command));
        // Search the command
        restCommandMockMvc.perform(get("/api/_search/commands?query=id:" + command.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(command.getId().intValue())))
            .andExpect(jsonPath("$.[*].sumprice").value(hasItem(DEFAULT_SUMPRICE)))
            .andExpect(jsonPath("$.[*].deliveryAddress").value(hasItem(DEFAULT_DELIVERY_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].delivery").value(hasItem(DEFAULT_DELIVERY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Command.class);
        Command command1 = new Command();
        command1.setId(1L);
        Command command2 = new Command();
        command2.setId(command1.getId());
        assertThat(command1).isEqualTo(command2);
        command2.setId(2L);
        assertThat(command1).isNotEqualTo(command2);
        command1.setId(null);
        assertThat(command1).isNotEqualTo(command2);
    }
}
