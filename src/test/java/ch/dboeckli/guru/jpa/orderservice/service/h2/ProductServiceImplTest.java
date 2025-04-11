package ch.dboeckli.guru.jpa.orderservice.service.h2;

import ch.dboeckli.guru.jpa.orderservice.domain.Product;
import ch.dboeckli.guru.jpa.orderservice.domain.ProductStatus;
import ch.dboeckli.guru.jpa.orderservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@Slf4j
class ProductServiceImplTest {

    @Autowired
    ProductService productService;

    @Test
    void updateQuantityOnHand() {
        Product product = new Product();
        product.setDescription("My Product");
        product.setProductStatus(ProductStatus.NEW);

        Product savedProduct = productService.saveProduct(product);
        assertEquals(0, savedProduct.getQuantityOnHand());

        Product savedProduct2 = productService.updateQuantityOnHand(savedProduct.getId(), 25);
        assertEquals(25, savedProduct2.getQuantityOnHand());
    }
}