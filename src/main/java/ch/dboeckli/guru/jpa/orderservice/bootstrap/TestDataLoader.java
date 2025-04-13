package ch.dboeckli.guru.jpa.orderservice.bootstrap;

import ch.dboeckli.guru.jpa.orderservice.domain.*;
import ch.dboeckli.guru.jpa.orderservice.repository.CustomerRepository;
import ch.dboeckli.guru.jpa.orderservice.repository.OrderHeaderRepository;
import ch.dboeckli.guru.jpa.orderservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataLoader implements CommandLineRunner {

    public static final String CUSTOMER_NAME_DEMO = "Customer For Single Order Demo";

    static final String PRODUCT_D1 = "Product 1";
    static final String PRODUCT_D2 = "Product 2";
    static final String PRODUCT_D3 = "Product 3";

    public static final String TEST_CUSTOMER = "TEST CUSTOMER";

    public static final int ORDERS_TO_CREATE = 100;

    private final OrderHeaderRepository orderHeaderRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    private final BootstrapOrderService bootstrapOrderService;

    @Override
    public void run(String... args) {
        OrderHeader singleOrderHeader = createSingleOrderHeader();

        /*
        demostation of following fact: https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html
        Method visibility and @Transactional in proxy mode

        The @Transactional annotation is typically used on methods with public visibility. As of 6.0, protected or package-visible methods can also be made transactional for class-based proxies by default.
        Note that transactional methods in interface-based proxies must always be public and defined in the proxied interface. For both kinds of proxies, only external method calls coming in through the proxy
        are intercepted.
         */
        demonstrateLazyLoading(singleOrderHeader.getId()); // Demonstrate lazy loading with LazyInitializationException
        bootstrapOrderService.demonstrateLazyLoading(singleOrderHeader.getId()); // Demonstrate lazy loading without LazyInitializationException

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

    private OrderHeader createSingleOrderHeader() {
        OrderHeader orderHeader = new OrderHeader();

        Customer customer = new Customer();
        customer.setCustomerName(CUSTOMER_NAME_DEMO);
        Customer savedCustomer = customerRepository.save(customer);

        orderHeader.setCustomer(savedCustomer);

        Product newProduct = new Product();
        newProduct.setProductStatus(ProductStatus.NEW);
        newProduct.setDescription("Product For Single Order  Demo");
        Product product = productRepository.saveAndFlush(newProduct);

        OrderLine orderLine = new OrderLine();
        orderLine.setQuantityOrdered(5);
        orderLine.setProduct(product);

        orderHeader.addOrderLine(orderLine);

        OrderApproval approval = new OrderApproval();
        approval.setApprovedBy("me");
        orderHeader.setOrderApproval(approval);

        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);
        orderHeaderRepository.flush();
        return savedOrder;
    }

    private void demonstrateLazyLoading(Long singleOrderHeaderId) {
        OrderHeader orderHeader = orderHeaderRepository.findById(singleOrderHeaderId).orElse(null);
        orderHeader.getOrderLines().forEach(orderLine -> {
            log.info("### Product Description: {}", orderLine.getProduct().getDescription());

            // here we get a lazy loading initializing exception
            try {
                orderLine.getProduct().getCategories().forEach(category -> log.info("### Category: {}", category.getDescription()));
            } catch (LazyInitializationException ex) {
                log.error("### Expected LazyInitializationException for demonstration", ex);
            }
        });
    }

    private void saveOrder(Customer customer, List<Product> products){
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setCustomer(customer);

        products.forEach(product -> {
            OrderLine orderLine = new OrderLine();
            orderLine.setProduct(product);
            orderLine.setQuantityOrdered(new SecureRandom().nextInt(20));
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
