package com.uga.gastorama.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.uga.gastorama.domain.Confectioner;
import com.uga.gastorama.repository.ConfectionerRepository;
import com.uga.gastorama.repository.search.ConfectionerSearchRepository;
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
 * REST controller for managing Confectioner.
 */
@RestController
@RequestMapping("/api")
public class ConfectionerResource {

    private final Logger log = LoggerFactory.getLogger(ConfectionerResource.class);

    private static final String ENTITY_NAME = "confectioner";

    private final ConfectionerRepository confectionerRepository;

    private final ConfectionerSearchRepository confectionerSearchRepository;

    public ConfectionerResource(ConfectionerRepository confectionerRepository, ConfectionerSearchRepository confectionerSearchRepository) {
        this.confectionerRepository = confectionerRepository;
        this.confectionerSearchRepository = confectionerSearchRepository;
    }

    /**
     * POST  /confectioners : Create a new confectioner.
     *
     * @param confectioner the confectioner to create
     * @return the ResponseEntity with status 201 (Created) and with body the new confectioner, or with status 400 (Bad Request) if the confectioner has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/confectioners")
    @Timed
    public ResponseEntity<Confectioner> createConfectioner(@Valid @RequestBody Confectioner confectioner) throws URISyntaxException {
        log.debug("REST request to save Confectioner : {}", confectioner);
        if (confectioner.getId() != null) {
            throw new BadRequestAlertException("A new confectioner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Confectioner result = confectionerRepository.save(confectioner);
        confectionerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/confectioners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /confectioners : Updates an existing confectioner.
     *
     * @param confectioner the confectioner to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated confectioner,
     * or with status 400 (Bad Request) if the confectioner is not valid,
     * or with status 500 (Internal Server Error) if the confectioner couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/confectioners")
    @Timed
    public ResponseEntity<Confectioner> updateConfectioner(@Valid @RequestBody Confectioner confectioner) throws URISyntaxException {
        log.debug("REST request to update Confectioner : {}", confectioner);
        if (confectioner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Confectioner result = confectionerRepository.save(confectioner);
        confectionerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, confectioner.getId().toString()))
            .body(result);
    }

    /**
     * GET  /confectioners : get all the confectioners.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of confectioners in body
     */
    @GetMapping("/confectioners")
    @Timed
    public List<Confectioner> getAllConfectioners() {
        log.debug("REST request to get all Confectioners");
        return confectionerRepository.findAll();
    }

    /**
     * GET  /confectioners/:id : get the "id" confectioner.
     *
     * @param id the id of the confectioner to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the confectioner, or with status 404 (Not Found)
     */
    @GetMapping("/confectioners/{id}")
    @Timed
    public ResponseEntity<Confectioner> getConfectioner(@PathVariable Long id) {
        log.debug("REST request to get Confectioner : {}", id);
        Optional<Confectioner> confectioner = confectionerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(confectioner);
    }

    /**
     * DELETE  /confectioners/:id : delete the "id" confectioner.
     *
     * @param id the id of the confectioner to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/confectioners/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfectioner(@PathVariable Long id) {
        log.debug("REST request to delete Confectioner : {}", id);

        confectionerRepository.deleteById(id);
        confectionerSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/confectioners?query=:query : search for the confectioner corresponding
     * to the query.
     *
     * @param query the query of the confectioner search
     * @return the result of the search
     */
    @GetMapping("/_search/confectioners")
    @Timed
    public List<Confectioner> searchConfectioners(@RequestParam String query) {
        log.debug("REST request to search Confectioners for query {}", query);
        return StreamSupport
            .stream(confectionerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
