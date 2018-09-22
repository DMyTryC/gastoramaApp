package com.uga.gastorama.service;

import com.uga.gastorama.domain.CommandLine;
import com.uga.gastorama.domain.Product;
import com.uga.gastorama.repository.CommandLineRepository;
import com.uga.gastorama.repository.ProductRepository;
import com.uga.gastorama.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for managing one Command Line.
 */
@Service
@Transactional
public class CommandLineService {

    public static final String NOT_ENOUGH_STOCK_EXCEPTION_MSG = "Not enough stock Exception";
    public static final String PRODUCT_DOES_NOT_EXIST = "Product doesn't exist";

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private ProductRepository productRepository;

    public boolean checkCommandLine (final CommandLine commandLine) {

        /*if (commandLine.getId() != null) {
            throw new BadRequestAlertException("A new commandLine cannot already have an ID", CommandLineResource.ENTITY_NAME, "idexists");
        }*/

        Product product = commandLine.getProduct();

        Optional<Product> dbProduct = productRepository.findById(product.getId());
        if(!dbProduct.isPresent()) {
            throw new CustomParameterizedException(PRODUCT_DOES_NOT_EXIST, product.toString());
        }

        commandLine.setProduct(dbProduct.get());
        product = commandLine.getProduct();

        if(commandLine.getQuantity() > product.getStock()) {

            throw new CustomParameterizedException(NOT_ENOUGH_STOCK_EXCEPTION_MSG, product.toString());
        }

        return true;
    }
}
