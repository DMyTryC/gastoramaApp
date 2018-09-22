package com.uga.gastorama.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.uga.gastorama.domain.ProductPhoto;
import com.uga.gastorama.repository.ProductPhotoRepository;
import com.uga.gastorama.repository.search.ProductPhotoSearchRepository;
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
 * REST controller for managing ProductPhoto.
 */
@RestController
@RequestMapping("/api")
public class ProductPhotoResource {

    private final Logger log = LoggerFactory.getLogger(ProductPhotoResource.class);

    private static final String ENTITY_NAME = "productPhoto";

    private final ProductPhotoRepository productPhotoRepository;

    private final ProductPhotoSearchRepository productPhotoSearchRepository;

    public ProductPhotoResource(ProductPhotoRepository productPhotoRepository, ProductPhotoSearchRepository productPhotoSearchRepository) {
        this.productPhotoRepository = productPhotoRepository;
        this.productPhotoSearchRepository = productPhotoSearchRepository;
    }

    /**
     * POST  /product-photos : Create a new productPhoto.
     *
     * @param productPhoto the productPhoto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productPhoto, or with status 400 (Bad Request) if the productPhoto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-photos")
    @Timed
    public ResponseEntity<ProductPhoto> createProductPhoto(@Valid @RequestBody ProductPhoto productPhoto) throws URISyntaxException {
        log.debug("REST request to save ProductPhoto : {}", productPhoto);
        if (productPhoto.getId() != null) {
            throw new BadRequestAlertException("A new productPhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductPhoto result = productPhotoRepository.save(productPhoto);
        productPhotoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/product-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-photos : Updates an existing productPhoto.
     *
     * @param productPhoto the productPhoto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productPhoto,
     * or with status 400 (Bad Request) if the productPhoto is not valid,
     * or with status 500 (Internal Server Error) if the productPhoto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-photos")
    @Timed
    public ResponseEntity<ProductPhoto> updateProductPhoto(@Valid @RequestBody ProductPhoto productPhoto) throws URISyntaxException {
        log.debug("REST request to update ProductPhoto : {}", productPhoto);
        if (productPhoto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductPhoto result = productPhotoRepository.save(productPhoto);
        productPhotoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productPhoto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-photos : get all the productPhotos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productPhotos in body
     */
    @GetMapping("/product-photos")
    @Timed
    public List<ProductPhoto> getAllProductPhotos() {
        log.debug("REST request to get all ProductPhotos");
        return productPhotoRepository.findAll();
    }

    /**
     * GET  /product-photos/:id : get the "id" productPhoto.
     *
     * @param id the id of the productPhoto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productPhoto, or with status 404 (Not Found)
     */
    @GetMapping("/product-photos/{id}")
    @Timed
    public ResponseEntity<ProductPhoto> getProductPhoto(@PathVariable Long id) {
        log.debug("REST request to get ProductPhoto : {}", id);
        Optional<ProductPhoto> productPhoto = productPhotoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productPhoto);
    }

    /**
     * DELETE  /product-photos/:id : delete the "id" productPhoto.
     *
     * @param id the id of the productPhoto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-photos/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductPhoto(@PathVariable Long id) {
        log.debug("REST request to delete ProductPhoto : {}", id);

        productPhotoRepository.deleteById(id);
        productPhotoSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-photos?query=:query : search for the productPhoto corresponding
     * to the query.
     *
     * @param query the query of the productPhoto search
     * @return the result of the search
     */
    @GetMapping("/_search/product-photos")
    @Timed
    public List<ProductPhoto> searchProductPhotos(@RequestParam String query) {
        log.debug("REST request to search ProductPhotos for query {}", query);
        return StreamSupport
            .stream(productPhotoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
