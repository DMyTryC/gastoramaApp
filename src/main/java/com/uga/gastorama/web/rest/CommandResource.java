package com.uga.gastorama.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.uga.gastorama.domain.Command;
import com.uga.gastorama.domain.CommandLine;
import com.uga.gastorama.domain.enumeration.State;
import com.uga.gastorama.repository.CommandLineRepository;
import com.uga.gastorama.repository.CommandRepository;
import com.uga.gastorama.repository.search.CommandLineSearchRepository;
import com.uga.gastorama.repository.search.CommandSearchRepository;
import com.uga.gastorama.service.CommandService;
import com.uga.gastorama.web.rest.errors.BadRequestAlertException;
import com.uga.gastorama.web.rest.errors.InternalServerErrorException;
import com.uga.gastorama.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Command.
 */
@RestController
@RequestMapping("/api")
public class CommandResource {

    private final Logger log = LoggerFactory.getLogger(CommandResource.class);

    private static final String ENTITY_NAME = "command";

    private final CommandRepository commandRepository;

    private final CommandSearchRepository commandSearchRepository;

    private final CommandService commandService;

    private final CommandLineSearchRepository commandLineSearchRepository;

    @Autowired
    private CommandLineRepository commandLineRepository;

    public CommandResource(CommandRepository commandRepository, CommandSearchRepository commandSearchRepository,
                           CommandLineSearchRepository commandLineSearchRepository, CommandService commandService) {
        this.commandRepository = commandRepository;
        this.commandSearchRepository = commandSearchRepository;
        this.commandLineSearchRepository = commandLineSearchRepository;
        this.commandService = commandService;
    }

    /**
     * POST  /commands : Create a new command and update stock accordingly.
     *
     * @param command the command to create
     * @return the ResponseEntity with status 201 (Created) and with body the new command, or with status 400 (Bad Request) if the command has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commands")
    @Timed
    @Transactional
    public ResponseEntity<Command> createCommand(@Valid @RequestBody Command command) throws URISyntaxException {
        log.debug("REST request to save Command : {}", command);
        if (command.getId() != null) {
            throw new BadRequestAlertException("A new command cannot already have an ID", ENTITY_NAME, "idexists");
        }

        command.setState(State.VALIDATED);
        commandService.checkCommandPossible(command);
        commandService.updateStock(command);

        for(CommandLine commandLine : command.getCommandLines()) {
            commandLine.setCommand(command);
        }

        Command result = commandRepository.save(command);
        //commandSearchRepository.save(result);


        // update command lines and save them
        for(CommandLine commandLine : result.getCommandLines()) {
            commandLine.setCommand(result);

            log.debug("REST request to save CommandLine : {}", commandLine);
            CommandLine commandLineResult = commandLineRepository.save(commandLine);
            //commandLineSearchRepository.save(commandLineResult);
        }

        return ResponseEntity.created(new URI("/api/commands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commands : Updates an existing command. Without updating the product's stocks.
     *
     * @param command the command to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated command,
     * or with status 400 (Bad Request) if the command is not valid,
     * or with status 500 (Internal Server Error) if the command couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/commands")
    @Timed
    public ResponseEntity<Command> updateCommand(@Valid @RequestBody Command command) throws URISyntaxException {
        log.debug("REST request to update Command : {}", command);
        if (command.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        // Check Command state (can't change command after it has been completed)

        commandService.checkCommandPossible(command);

        Command result = commandRepository.save(command);
        commandSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, command.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commands : get all the commands.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of commands in body
     */
    @GetMapping("/commands")
    @Timed
    public List<Command> getAllCommands() {
        log.debug("REST request to get all Commands");
        return commandRepository.findAll();
    }

    /**
     * GET  /commands/:id : get the "id" command.
     *
     * @param id the id of the command to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the command, or with status 404 (Not Found)
     */
    @GetMapping("/commands/{id}")
    @Timed
    public ResponseEntity<Command> getCommand(@PathVariable Long id) {
        log.debug("REST request to get Command : {}", id);
        Optional<Command> command = commandRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(command);
    }

    /**
     * DELETE  /commands/:id : delete the "id" command.
     *
     * @param id the id of the command to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commands/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
        log.debug("REST request to delete Command : {}", id);

        commandRepository.deleteById(id);
        commandSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/commands?query=:query : search for the command corresponding
     * to the query.
     *
     * @param query the query of the command search
     * @return the result of the search
     */
    @GetMapping("/_search/commands")
    @Timed
    public List<Command> searchCommands(@RequestParam String query) {
        log.debug("REST request to search Commands for query {}", query);
        return StreamSupport
            .stream(commandSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
