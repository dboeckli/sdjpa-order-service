package ch.dboeckli.guru.jpa.orderservice.repository.h2;

import ch.dboeckli.guru.jpa.orderservice.domain.*;
import ch.dboeckli.guru.jpa.orderservice.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Slf4j
public class OrderHeaderRepositoryTest {

    @Autowired
    OrderHeaderRepository orderHeaderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderLineRepository orderLineRepository;

    @Autowired
    OrderApprovalRepository orderApprovalRepository;

    Product product;

    @BeforeEach
    void setUp() {
        Product newProduct = new Product();
        newProduct.setProductStatus(ProductStatus.NEW);
        newProduct.setDescription("test product");
        product = productRepository.saveAndFlush(newProduct);
    }

    @Test
    void testSaveOrderWithLine() {
        OrderHeader orderHeader = new OrderHeader();

        Customer customer = new Customer();
        customer.setCustomerName("New Customer");
        Customer savedCustomer = customerRepository.save(customer);

        orderHeader.setCustomer(savedCustomer);

        OrderLine orderLine = new OrderLine();
        orderLine.setQuantityOrdered(5);
        orderLine.setProduct(product);

        orderHeader.addOrderLine(orderLine);

        OrderApproval approval = new OrderApproval();
        approval.setApprovedBy("me");
        orderHeader.setOrderApproval(approval);

        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);
        orderHeaderRepository.flush();

        assertAll("Saved Order",
            () -> assertNotNull(savedOrder),
            () -> assertNotNull(savedOrder.getId()),
            () -> assertNotNull(savedOrder.getOrderLines()),
            () -> assertEquals(1, savedOrder.getOrderLines().size()),
            () -> assertNotNull(savedOrder.getOrderLines().iterator().next().getId())
        );

        OrderHeader fetchedOrder = orderHeaderRepository.getReferenceById(savedOrder.getId());
        assertAll("Fetched Order",
            () -> assertNotNull(fetchedOrder),
            () -> assertEquals(1, fetchedOrder.getOrderLines().size()),
            () -> assertNotNull(fetchedOrder.getOrderLines().iterator().next().getProduct().getId()),
            () -> assertNotNull(fetchedOrder.getCustomer().getId()),
            () -> assertNotNull(fetchedOrder.getOrderApproval().getId())
        );
    }

    @Test
    void testSaveOrder() {
        OrderHeader orderHeader = new OrderHeader();

        Customer customer = new Customer();
        customer.setCustomerName("New Customer");
        Customer savedCustomer = customerRepository.save(customer);

        orderHeader.setCustomer(savedCustomer);
        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);

        assertAll("Saved Order",
            () -> assertNotNull(savedOrder),
            () -> assertNotNull(savedOrder.getId())
        );

        OrderHeader fetchedOrder = orderHeaderRepository.getReferenceById(savedOrder.getId());

        assertAll("Fetched Order",
            () -> assertNotNull(fetchedOrder),
            () -> assertNotNull(fetchedOrder.getId()),
            () -> assertNotNull(fetchedOrder.getCreatedDate()),
            () -> assertNotNull(fetchedOrder.getLastModifiedDate()),
            () -> assertNotNull(fetchedOrder.getCustomer().getId())
        );
    }

    @Test
    void testDeleteCascade() {

        OrderHeader orderHeader = new OrderHeader();
        Customer customer = new Customer();
        customer.setCustomerName("new Customer");
        orderHeader.setCustomer(customerRepository.save(customer));

        OrderLine orderLine1 = new OrderLine();
        orderLine1.setQuantityOrdered(3);
        orderLine1.setProduct(product);

        OrderLine orderLine2 = new OrderLine();
        orderLine2.setQuantityOrdered(2);
        orderLine2.setProduct(product);

        orderHeader.addOrderLine(orderLine1);
        orderHeader.addOrderLine(orderLine2);

        OrderApproval orderApproval = new OrderApproval();
        orderApproval.setApprovedBy("me");
        orderHeader.setOrderApproval(orderApproval);

        OrderHeader savedOrder = orderHeaderRepository.saveAndFlush(orderHeader);
        log.info("order saved and flushed");

        List<Long> orderLineIds = savedOrder.getOrderLines().stream()
            .map(OrderLine::getId)
            .toList();

        orderHeaderRepository.deleteById(savedOrder.getId());
        orderHeaderRepository.flush();

        assertAll("Fetched Order",
            () -> assertFalse(orderHeaderRepository.existsById(savedOrder.getId())),
            () -> {
                for (Long orderLineId : orderLineIds) {
                    assertFalse(orderLineRepository.existsById(orderLineId), "OrderLine should be deleted: " + orderLineId);
                }
            },
            () -> assertFalse(orderApprovalRepository.existsById(savedOrder.getOrderApproval().getId()), "OrderApproval should be deleted: " + savedOrder.getOrderApproval().getId())
        );
    }
}
