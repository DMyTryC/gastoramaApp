package com.uga.gastorama.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.uga.gastorama.domain.CommandLine;
import com.uga.gastorama.repository.CommandLineRepository;
import com.uga.gastorama.repository.search.CommandLineSearchRepository;
import com.uga.gastorama.web.rest.errors.BadRequestAlertException;
import com.uga.gastorama.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
 * REST controller for managing CommandLine.
 */
@RestController
@RequestMapping("/api")
public class CommandLineResource {

    private final Logger log = LoggerFactory.getLogger(CommandLineResource.class);

    public static final String ENTITY_NAME = "commandLine";

    private final CommandLineRepository commandLineRepository;

    private final CommandLineSearchRepository commandLineSearchRepository;

    public CommandLineResource(CommandLineRepository commandLineRepository, CommandLineSearchRepository commandLineSearchRepository) {
        this.commandLineRepository = commandLineRepository;
        this.commandLineSearchRepository = commandLineSearchRepository;
    }

    /**
     * POST  /command-lines : Create a new commandLine.
     *
     * @param commandLine the commandLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commandLine, or with status 400 (Bad Request) if the commandLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    /*@PostMapping("/command-lines")
    @Timed
    public ResponseEntity<CommandLine> createCommandLine(@Valid @RequestBody CommandLine commandLine) throws URISyntaxException {
        log.debug("REST request to save CommandLine : {}", commandLine);
        if (commandLine.getId() != null) {
            throw new BadRequestAlertException("A new commandLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommandLine result = commandLineRepository.save(commandLine);
        commandLineSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/command-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * PUT  /command-lines : Updates an existing commandLine.
     *
     * @param commandLine the commandLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commandLine,
     * or with status 400 (Bad Request) if the commandLine is not valid,
     * or with status 500 (Internal Server Error) if the commandLine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    /*@PutMapping("/command-lines")
    @Timed
    public ResponseEntity<CommandLine> updateCommandLine(@Valid @RequestBody CommandLine commandLine) throws URISyntaxException {
        log.debug("REST request to update CommandLine : {}", commandLine);
        if (commandLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommandLine result = commandLineRepository.save(commandLine);
        commandLineSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, commandLine.getId().toString()))
            .body(result);
    }*/

    /**
     * GET  /command-lines : get all the commandLines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of commandLines in body
     */
    @GetMapping("/command-lines")
    @Timed
    public List<CommandLine> getAllCommandLines() {
        log.debug("REST request to get all CommandLines");
        return commandLineRepository.findAll();
    }

    /**
     * GET  /command-lines/:id : get the "id" commandLine.
     *
     * @param id the id of the commandLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commandLine, or with status 404 (Not Found)
     */
    @GetMapping("/command-lines/{id}")
    @Timed
    public ResponseEntity<CommandLine> getCommandLine(@PathVariable Long id) {
        log.debug("REST request to get CommandLine : {}", id);
        Optional<CommandLine> commandLine = commandLineRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(commandLine);
    }

    /**
     * DELETE  /command-lines/:id : delete the "id" commandLine.
     *
     * @param id the id of the commandLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    /*@DeleteMapping("/command-lines/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommandLine(@PathVariable Long id) {
        log.debug("REST request to delete CommandLine : {}", id);

        commandLineRepository.deleteById(id);
        commandLineSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }*/

    /**
     * SEARCH  /_search/command-lines?query=:query : search for the commandLine corresponding
     * to the query.
     *
     * @param query the query of the commandLine search
     * @return the result of the search
     */
    @GetMapping("/_search/command-lines")
    @Timed
    public List<CommandLine> searchCommandLines(@RequestParam String query) {
        log.debug("REST request to search CommandLines for query {}", query);
        return StreamSupport
            .stream(commandLineSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
