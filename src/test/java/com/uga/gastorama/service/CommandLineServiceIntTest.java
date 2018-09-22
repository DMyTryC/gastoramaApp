package com.uga.gastorama.service;

import com.uga.gastorama.GastoramaApp;
import com.uga.gastorama.domain.CommandLine;
import com.uga.gastorama.domain.Product;
import com.uga.gastorama.repository.CommandLineRepository;
import com.uga.gastorama.repository.ProductRepository;
import com.uga.gastorama.web.rest.errors.CustomParameterizedException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.Instant;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test class for the CommandLine REST controller.
 *
 * @see CommandLineService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GastoramaApp.class)
public class CommandLineServiceIntTest {

    //@Autowired
    //private CommandLineRepository commandLineRepository;

    @Autowired
    private CommandLineService commandLineService;

    @Autowired
    private ProductRepository productRepository;

    private CommandLine commandLine;

    public CommandLineServiceIntTest() {
    }

    @Before
    public void init() {
        commandLine = new CommandLine();

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
    }

    @Test
    @Transactional
    public void asserThatEnoughQty () {

        commandLine.setQuantity(4);
        //commandLineRepository.saveAndFlush(commandLine);

        try {
            commandLineService.checkCommandLine(commandLine);
            fail("No exception with msg : " + CommandLineService.NOT_ENOUGH_STOCK_EXCEPTION_MSG);
        } catch (CustomParameterizedException e) {

            CustomParameterizedException expectedException = new CustomParameterizedException(
                    CommandLineService.NOT_ENOUGH_STOCK_EXCEPTION_MSG, commandLine.getProduct().toString());
            assertEquals(expectedException.getDetail(), e.getDetail());
        }
    }
}
