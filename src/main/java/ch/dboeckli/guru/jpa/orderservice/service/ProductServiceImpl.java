package ch.dboeckli.guru.jpa.orderservice.service;

import ch.dboeckli.guru.jpa.orderservice.domain.Product;
import ch.dboeckli.guru.jpa.orderservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.saveAndFlush(product);
    }

    @Override
    @Transactional
    public Product updateQuantityOnHand(Long id, Integer quantityOnHand) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setQuantityOnHand(quantityOnHand);
        return productRepository.saveAndFlush(product);
    }
}
