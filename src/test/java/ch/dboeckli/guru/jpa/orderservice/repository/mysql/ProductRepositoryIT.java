package ch.dboeckli.guru.jpa.orderservice.repository.mysql;

import ch.dboeckli.guru.jpa.orderservice.domain.Product;
import ch.dboeckli.guru.jpa.orderservice.domain.ProductStatus;
import ch.dboeckli.guru.jpa.orderservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test_mysql")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class ProductRepositoryIT {

    @Autowired
    ProductRepository productRepository;

    @Test
    void testGetCategory() {
        Optional<Product> productOptional = productRepository.findByDescription("PRODUCT1");
        assertAll(
            () -> assertTrue(productOptional.isPresent(), "Product should be present"),
            () -> {
                Product product = productOptional.get();
                assertAll(
                    () -> assertNotNull(product, "Product should not be null"),
                    () -> assertNotNull(product.getCategories(), "Product categories should not be null"),
                    () -> assertEquals(2, product.getCategories().size(), "Product should have 2 categories")
                );
            }
        );
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setDescription("My Product");
        product.setProductStatus(ProductStatus.NEW);

        Product savedProduct = productRepository.save(product);

        Product fetchedProduct = productRepository.getReferenceById(savedProduct.getId());

        assertAll(
            () -> assertNotNull(fetchedProduct, "Fetched product should not be null"),
            () -> assertNotNull(fetchedProduct.getDescription(), "Product description should not be null"),
            () -> assertEquals("My Product", fetchedProduct.getDescription(), "Product description should match"),
            () -> assertNotNull(fetchedProduct.getCreatedDate(), "Created date should not be null"),
            () -> assertNotNull(fetchedProduct.getLastModifiedDate(), "Last modified date should not be null"),
            () -> assertEquals(ProductStatus.NEW, fetchedProduct.getProductStatus(), "Product status should be NEW")
        );
    }

    @Test
    void addAndUpdateProduct() {
        Product product = new Product();
        product.setDescription("My Product");
        product.setProductStatus(ProductStatus.NEW);

        Product savedProduct = productRepository.saveAndFlush(product);
        assertEquals(0, savedProduct.getQuantityOnHand());

        savedProduct.setQuantityOnHand(25);

        Product savedProduct2 = productRepository.saveAndFlush(savedProduct);
        assertEquals(25, savedProduct2.getQuantityOnHand());
    }

}
