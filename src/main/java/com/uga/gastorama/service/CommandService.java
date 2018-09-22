package com.uga.gastorama.service;

import com.uga.gastorama.domain.Command;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Service class for managing stocks.
 */
@Service
@Transactional
public class CommandService {

    public static final String EMPTY_COMMAND_EXCEPTION_MSG = "Empty Command Exception";
    public static final String NOT_ENOUGH_STOCK_EXCEPTION_MSG = "Not enough stock Exception";

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private CommandLineService commandLineService;

    @Autowired
    private ProductService productService;

    /**
     * Check if a command can be done. It will check stocks for each products.
     * @param command The command to check
     * @return True if valid command, false otherwise
     */
    public boolean checkCommandPossible(Command command) {

        Set<CommandLine> commandLines = command.getCommandLines();

        if(commandLines.isEmpty()) {

            throw new CustomParameterizedException(EMPTY_COMMAND_EXCEPTION_MSG);
        }

        // Check each command line
        for (CommandLine commandLine : commandLines) {
            commandLineService.checkCommandLine(commandLine);
        }

        return true;
    }

    /**
     * Update the stock of a product according to a command changes.
     * This will save in DB the product's stock and command Lines.
     * @param command The command that will change the stock
     */
    public void updateStock(Command command) {

        Set<CommandLine> commandLines = command.getCommandLines();

        // Check each command line
        for (CommandLine commandLine : commandLines) {
            Product product = commandLine.getProduct();

            int newQty = product.getStock() - commandLine.getQuantity();

            if(newQty <= 0) {
                throw new CustomParameterizedException(NOT_ENOUGH_STOCK_EXCEPTION_MSG, product.toString(), commandLine.toString());
            }

            productService.updateStock(product, newQty);
        }
    }
}
