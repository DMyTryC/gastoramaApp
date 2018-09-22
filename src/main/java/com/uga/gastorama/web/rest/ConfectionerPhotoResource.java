package com.uga.gastorama.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.uga.gastorama.domain.ConfectionerPhoto;
import com.uga.gastorama.repository.ConfectionerPhotoRepository;
import com.uga.gastorama.repository.search.ConfectionerPhotoSearchRepository;
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
 * REST controller for managing ConfectionerPhoto.
 */
@RestController
@RequestMapping("/api")
public class ConfectionerPhotoResource {

    private final Logger log = LoggerFactory.getLogger(ConfectionerPhotoResource.class);

    private static final String ENTITY_NAME = "confectionerPhoto";

    private final ConfectionerPhotoRepository confectionerPhotoRepository;

    private final ConfectionerPhotoSearchRepository confectionerPhotoSearchRepository;

    public ConfectionerPhotoResource(ConfectionerPhotoRepository confectionerPhotoRepository, ConfectionerPhotoSearchRepository confectionerPhotoSearchRepository) {
        this.confectionerPhotoRepository = confectionerPhotoRepository;
        this.confectionerPhotoSearchRepository = confectionerPhotoSearchRepository;
    }

    /**
     * POST  /confectioner-photos : Create a new confectionerPhoto.
     *
     * @param confectionerPhoto the confectionerPhoto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new confectionerPhoto, or with status 400 (Bad Request) if the confectionerPhoto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/confectioner-photos")
    @Timed
    public ResponseEntity<ConfectionerPhoto> createConfectionerPhoto(@Valid @RequestBody ConfectionerPhoto confectionerPhoto) throws URISyntaxException {
        log.debug("REST request to save ConfectionerPhoto : {}", confectionerPhoto);
        if (confectionerPhoto.getId() != null) {
            throw new BadRequestAlertException("A new confectionerPhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfectionerPhoto result = confectionerPhotoRepository.save(confectionerPhoto);
        confectionerPhotoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/confectioner-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /confectioner-photos : Updates an existing confectionerPhoto.
     *
     * @param confectionerPhoto the confectionerPhoto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated confectionerPhoto,
     * or with status 400 (Bad Request) if the confectionerPhoto is not valid,
     * or with status 500 (Internal Server Error) if the confectionerPhoto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/confectioner-photos")
    @Timed
    public ResponseEntity<ConfectionerPhoto> updateConfectionerPhoto(@Valid @RequestBody ConfectionerPhoto confectionerPhoto) throws URISyntaxException {
        log.debug("REST request to update ConfectionerPhoto : {}", confectionerPhoto);
        if (confectionerPhoto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfectionerPhoto result = confectionerPhotoRepository.save(confectionerPhoto);
        confectionerPhotoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, confectionerPhoto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /confectioner-photos : get all the confectionerPhotos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of confectionerPhotos in body
     */
    @GetMapping("/confectioner-photos")
    @Timed
    public List<ConfectionerPhoto> getAllConfectionerPhotos() {
        log.debug("REST request to get all ConfectionerPhotos");
        return confectionerPhotoRepository.findAll();
    }

    /**
     * GET  /confectioner-photos/:id : get the "id" confectionerPhoto.
     *
     * @param id the id of the confectionerPhoto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the confectionerPhoto, or with status 404 (Not Found)
     */
    @GetMapping("/confectioner-photos/{id}")
    @Timed
    public ResponseEntity<ConfectionerPhoto> getConfectionerPhoto(@PathVariable Long id) {
        log.debug("REST request to get ConfectionerPhoto : {}", id);
        Optional<ConfectionerPhoto> confectionerPhoto = confectionerPhotoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(confectionerPhoto);
    }

    /**
     * DELETE  /confectioner-photos/:id : delete the "id" confectionerPhoto.
     *
     * @param id the id of the confectionerPhoto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/confectioner-photos/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfectionerPhoto(@PathVariable Long id) {
        log.debug("REST request to delete ConfectionerPhoto : {}", id);

        confectionerPhotoRepository.deleteById(id);
        confectionerPhotoSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/confectioner-photos?query=:query : search for the confectionerPhoto corresponding
     * to the query.
     *
     * @param query the query of the confectionerPhoto search
     * @return the result of the search
     */
    @GetMapping("/_search/confectioner-photos")
    @Timed
    public List<ConfectionerPhoto> searchConfectionerPhotos(@RequestParam String query) {
        log.debug("REST request to search ConfectionerPhotos for query {}", query);
        return StreamSupport
            .stream(confectionerPhotoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
