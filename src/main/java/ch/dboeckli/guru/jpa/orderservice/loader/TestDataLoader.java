package ch.dboeckli.guru.jpa.orderservice.loader;

import ch.dboeckli.guru.jpa.orderservice.domain.*;
import ch.dboeckli.guru.jpa.orderservice.repository.CustomerRepository;
import ch.dboeckli.guru.jpa.orderservice.repository.OrderHeaderRepository;
import ch.dboeckli.guru.jpa.orderservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataLoader implements CommandLineRunner {

    final String PRODUCT_D1 = "Product 1";
    final String PRODUCT_D2 = "Product 2";
    final String PRODUCT_D3 = "Product 3";

    final String TEST_CUSTOMER = "TEST CUSTOMER";

    public final static int ORDERS_TO_CREATE = 100;

    private final OrderHeaderRepository orderHeaderRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        log.info("### Loading test data...");
        List<Product> products = loadProducts();
        Customer customer = loadCustomers();

        for (int i = 0; i < ORDERS_TO_CREATE; i++) {
            log.info("Creating order #: " + i);
            saveOrder(customer, products);
        }

        orderHeaderRepository.flush();
        log.info("### Test data loaded successfully! {} orders created.", ORDERS_TO_CREATE);
    }

    private void saveOrder(Customer customer, List<Product> products){
        Random random = new Random();

        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setCustomer(customer);

        products.forEach(product -> {
            OrderLine orderLine = new OrderLine();
            orderLine.setProduct(product);
            orderLine.setQuantityOrdered(random.nextInt(20));
            orderHeader.addOrderLine(orderLine);
        });
        orderHeaderRepository.save(orderHeader);
    }

    private Customer loadCustomers() {
        return getOrSaveCustomer(TEST_CUSTOMER);
    }

    private Customer getOrSaveCustomer(String customerName) {
        return customerRepository.findCustomerByCustomerNameIgnoreCase(customerName)
            .orElseGet(() -> {
                Customer c1 = new Customer();
                c1.setCustomerName(customerName);
                c1.setEmail("test@example.com");
                Address address = new Address();
                address.setAddress("123 Main");
                address.setCity("New Orleans");
                address.setState("LA");
                c1.setAddress(address);
                return customerRepository.save(c1);
            });
    }
    private List<Product> loadProducts(){
        List<Product> products = new ArrayList<>();

        products.add(getOrSaveProduct(PRODUCT_D1));
        products.add(getOrSaveProduct(PRODUCT_D2));
        products.add(getOrSaveProduct(PRODUCT_D3));

        return products;
    }
    private Product getOrSaveProduct(String description) {
        return productRepository.findByDescription(description)
            .orElseGet(() -> {
                Product p1 = new Product();
                p1.setDescription(description);
                p1.setProductStatus(ProductStatus.NEW);
                return productRepository.save(p1);
            });
    }
}
