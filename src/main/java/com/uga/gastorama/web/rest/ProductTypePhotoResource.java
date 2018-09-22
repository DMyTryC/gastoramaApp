package com.uga.gastorama.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.uga.gastorama.domain.ProductTypePhoto;
import com.uga.gastorama.repository.ProductTypePhotoRepository;
import com.uga.gastorama.repository.search.ProductTypePhotoSearchRepository;
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
 * REST controller for managing ProductTypePhoto.
 */
@RestController
@RequestMapping("/api")
public class ProductTypePhotoResource {

    private final Logger log = LoggerFactory.getLogger(ProductTypePhotoResource.class);

    private static final String ENTITY_NAME = "productTypePhoto";

    private final ProductTypePhotoRepository productTypePhotoRepository;

    private final ProductTypePhotoSearchRepository productTypePhotoSearchRepository;

    public ProductTypePhotoResource(ProductTypePhotoRepository productTypePhotoRepository, ProductTypePhotoSearchRepository productTypePhotoSearchRepository) {
        this.productTypePhotoRepository = productTypePhotoRepository;
        this.productTypePhotoSearchRepository = productTypePhotoSearchRepository;
    }

    /**
     * POST  /product-type-photos : Create a new productTypePhoto.
     *
     * @param productTypePhoto the productTypePhoto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productTypePhoto, or with status 400 (Bad Request) if the productTypePhoto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-type-photos")
    @Timed
    public ResponseEntity<ProductTypePhoto> createProductTypePhoto(@Valid @RequestBody ProductTypePhoto productTypePhoto) throws URISyntaxException {
        log.debug("REST request to save ProductTypePhoto : {}", productTypePhoto);
        if (productTypePhoto.getId() != null) {
            throw new BadRequestAlertException("A new productTypePhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductTypePhoto result = productTypePhotoRepository.save(productTypePhoto);
        productTypePhotoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/product-type-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-type-photos : Updates an existing productTypePhoto.
     *
     * @param productTypePhoto the productTypePhoto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productTypePhoto,
     * or with status 400 (Bad Request) if the productTypePhoto is not valid,
     * or with status 500 (Internal Server Error) if the productTypePhoto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-type-photos")
    @Timed
    public ResponseEntity<ProductTypePhoto> updateProductTypePhoto(@Valid @RequestBody ProductTypePhoto productTypePhoto) throws URISyntaxException {
        log.debug("REST request to update ProductTypePhoto : {}", productTypePhoto);
        if (productTypePhoto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductTypePhoto result = productTypePhotoRepository.save(productTypePhoto);
        productTypePhotoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productTypePhoto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-type-photos : get all the productTypePhotos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productTypePhotos in body
     */
    @GetMapping("/product-type-photos")
    @Timed
    public List<ProductTypePhoto> getAllProductTypePhotos() {
        log.debug("REST request to get all ProductTypePhotos");
        return productTypePhotoRepository.findAll();
    }

    /**
     * GET  /product-type-photos/:id : get the "id" productTypePhoto.
     *
     * @param id the id of the productTypePhoto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productTypePhoto, or with status 404 (Not Found)
     */
    @GetMapping("/product-type-photos/{id}")
    @Timed
    public ResponseEntity<ProductTypePhoto> getProductTypePhoto(@PathVariable Long id) {
        log.debug("REST request to get ProductTypePhoto : {}", id);
        Optional<ProductTypePhoto> productTypePhoto = productTypePhotoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productTypePhoto);
    }

    /**
     * DELETE  /product-type-photos/:id : delete the "id" productTypePhoto.
     *
     * @param id the id of the productTypePhoto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-type-photos/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductTypePhoto(@PathVariable Long id) {
        log.debug("REST request to delete ProductTypePhoto : {}", id);

        productTypePhotoRepository.deleteById(id);
        productTypePhotoSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-type-photos?query=:query : search for the productTypePhoto corresponding
     * to the query.
     *
     * @param query the query of the productTypePhoto search
     * @return the result of the search
     */
    @GetMapping("/_search/product-type-photos")
    @Timed
    public List<ProductTypePhoto> searchProductTypePhotos(@RequestParam String query) {
        log.debug("REST request to search ProductTypePhotos for query {}", query);
        return StreamSupport
            .stream(productTypePhotoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
