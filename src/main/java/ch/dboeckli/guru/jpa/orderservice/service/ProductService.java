package ch.dboeckli.guru.jpa.orderservice.service;

import ch.dboeckli.guru.jpa.orderservice.domain.Product;

public interface ProductService {

    Product saveProduct(Product product);

    Product updateQuantityOnHand(Long id, Integer quantityOnHand);

}
