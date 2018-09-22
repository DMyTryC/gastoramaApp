package com.uga.gastorama.service;

import com.uga.gastorama.domain.Product;
import com.uga.gastorama.repository.ProductRepository;
import com.uga.gastorama.web.rest.errors.CustomParameterizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing products.
 */
@Service
@Transactional
public class ProductService {

    public static final String INVALID_STOCK_EXCEPTION_MSG = "Invalid stock";

    @Autowired
    private ProductRepository productRepository;

    public void updateStock(Product product, int quantity) {

        if(quantity < 0) {
            throw new CustomParameterizedException(INVALID_STOCK_EXCEPTION_MSG, Integer.toString(quantity));
        }

        product.setStock(quantity);

        productRepository.save(product);
    }
}
