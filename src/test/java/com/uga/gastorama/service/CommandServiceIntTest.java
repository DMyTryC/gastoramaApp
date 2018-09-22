package com.uga.gastorama.service;

import com.uga.gastorama.GastoramaApp;
import com.uga.gastorama.domain.Command;
import com.uga.gastorama.domain.CommandLine;
import com.uga.gastorama.domain.Product;
import com.uga.gastorama.domain.enumeration.DeliveryMode;
import com.uga.gastorama.domain.enumeration.State;
import com.uga.gastorama.repository.CommandRepository;
import com.uga.gastorama.repository.ProductRepository;
import com.uga.gastorama.web.rest.errors.CustomParameterizedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for the CommandResource REST controller.
 *
 * @see CommandService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GastoramaApp.class)
@Transactional
public class CommandServiceIntTest {

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private CommandService commandService;

    @Autowired
    private ProductRepository productRepository;

    private Command command;

    public CommandServiceIntTest() {
    }

    @Before
    public void init() {

        command = new Command();
        command.setDate(Instant.now());
        command.setDelivery(DeliveryMode.DOMICILE);
        command.setDeliveryAddress("Test address");
        command.setState(State.VALIDATED);
        command.setSumprice(0);
    }

    public void populateCommand(Command command) {

        Set<CommandLine> commandLineSet = new HashSet<CommandLine>();

        CommandLine commandLine = new CommandLine();

        Product product = new Product();
        product.setName("Meringue");
        product.setPrice(50);
        product.setStock(3);
        product.setWeight(100);
        product.setId((long) 1);
        product.setPassDate(Instant.now());
        product.setPieces(1);
        productRepository.saveAndFlush(product);

        commandLine.setProduct(product);
        commandLine.setQuantity(1);

        commandLineSet.add(commandLine);

        commandLine.setCommand(command);
        command.setCommandLines(commandLineSet);

    }

    @Test
    public void assertThatCommandNotEmpty() {

        commandRepository.saveAndFlush(command);

        try {
            commandService.checkCommandPossible(command);
        } catch (CustomParameterizedException e) {
            CustomParameterizedException expectedException = new CustomParameterizedException(
                CommandService.EMPTY_COMMAND_EXCEPTION_MSG);
            assertEquals(expectedException.getDetail(), e.getDetail());
        }
    }

    @Test
    public void assertThatCommandUpdateStocks() {

        populateCommand(command);
        commandRepository.saveAndFlush(command);

        Map<CommandLine, Integer> expectedStockMap = new HashMap<CommandLine, Integer>();

        for (CommandLine commandLine : command.getCommandLines()) {

            int expectedStock = commandLine.getProduct().getStock() - commandLine.getQuantity();

            expectedStockMap.put(commandLine, expectedStock);
        }

        commandService.updateStock(command);

        for (CommandLine commandLine : command.getCommandLines()) {

            Product product = commandLine.getProduct();

            int expectedStock = expectedStockMap.get(commandLine);

            assertNotEquals(true, expectedStock < 0);
            assertEquals((long)expectedStock, (long)product.getStock());
        }
    }
}
