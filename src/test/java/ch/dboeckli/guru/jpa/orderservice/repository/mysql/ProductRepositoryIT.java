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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test_mysql")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class ProductRepositoryIT {

    @Autowired
    ProductRepository productRepository;

    @Test
    void testGetCategory() {
        Product product = productRepository.findByDescription("PRODUCT1");

        assertNotNull(product);
        assertNotNull(product.getCategories());
        assertEquals(2, product.getCategories().size());
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setDescription("My Product");
        product.setProductStatus(ProductStatus.NEW);

        Product savedProduct = productRepository.save(product);

        Product fetchedProduct = productRepository.getReferenceById(savedProduct.getId());

        assertNotNull(fetchedProduct);
        assertNotNull(fetchedProduct.getDescription());
        assertNotNull(fetchedProduct.getCreatedDate());
        assertNotNull(fetchedProduct.getLastModifiedDate());
    }

}
