package org.temire.reporting.service.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.temire.reporting.service.data.dto.OrderProduct;
import org.temire.reporting.service.data.model.Order;
import org.temire.reporting.service.data.model.Product;
import org.temire.reporting.service.data.repositories.OrderRepository;
import org.temire.reporting.service.data.repositories.ProductRepository;
import org.temire.reporting.service.rest.response.GenericResponseDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository mockOrderRepository;
    @Mock
    private ProductRepository mockProductRepository;

    private OrderServiceImpl orderServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        orderServiceImplUnderTest = new OrderServiceImpl(mockOrderRepository, mockProductRepository);
    }

    @Test
    void testFindAll() {
        // Setup
        // Configure OrderRepository.findAll(...).
        final Page<Order> orders = new PageImpl<>(
                List.of(new Order("order_id", List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)),
                        0.0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), "delivery_address", "customer_name",
                        "customer_email", "customer_phone", false)));
        when(mockOrderRepository.findAll(any(Pageable.class))).thenReturn(orders);

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.findAll(PageRequest.of(0, 1));

        // Verify the results
    }

    @Test
    void testFindAll_OrderRepositoryReturnsNoItems() {
        // Setup
        when(mockOrderRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.findAll(PageRequest.of(0, 1));

        // Verify the results
    }

    @Test
    void testByRange() {
        // Setup
        // Configure OrderRepository.findByOrder_dateBetween(...).
        final List<Order> orders = List.of(
                new Order("order_id", List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0,
                        LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), "delivery_address", "customer_name",
                        "customer_email", "customer_phone", false));
        when(mockOrderRepository.findByOrder_dateBetween(eq(LocalDate.of(2020, 1, 1)), eq(LocalDate.of(2020, 1, 1)),
                any(Pageable.class))).thenReturn(orders);

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.byRange(PageRequest.of(0, 1),
                LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1));

        // Verify the results
    }

    @Test
    void testByRange_OrderRepositoryReturnsNoItems() {
        // Setup
        when(mockOrderRepository.findByOrder_dateBetween(eq(LocalDate.of(2020, 1, 1)), eq(LocalDate.of(2020, 1, 1)),
                any(Pageable.class))).thenReturn(Collections.emptyList());

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.byRange(PageRequest.of(0, 1),
                LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1));

        // Verify the results
    }

    @Test
    void testByRangeAndAmount() {
        // Setup
        // Configure OrderRepository.findByOrder_dateBetweenAndAmount(...).
        final List<Order> orders = List.of(
                new Order("order_id", List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0,
                        LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), "delivery_address", "customer_name",
                        "customer_email", "customer_phone", false));
        when(mockOrderRepository.findByOrder_dateBetweenAndAmount(eq(LocalDate.of(2020, 1, 1)),
                eq(LocalDate.of(2020, 1, 1)), eq(0.0), any(Pageable.class))).thenReturn(orders);

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.byRangeAndAmount(PageRequest.of(0, 1),
                LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), 0.0);

        // Verify the results
    }

    @Test
    void testByRangeAndAmount_OrderRepositoryReturnsNoItems() {
        // Setup
        when(mockOrderRepository.findByOrder_dateBetweenAndAmount(eq(LocalDate.of(2020, 1, 1)),
                eq(LocalDate.of(2020, 1, 1)), eq(0.0), any(Pageable.class))).thenReturn(Collections.emptyList());

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.byRangeAndAmount(PageRequest.of(0, 1),
                LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), 0.0);

        // Verify the results
    }

    @Test
    void testFindById() {
        // Setup
        // Configure OrderRepository.findById(...).
        final Optional<Order> order = Optional.of(
                new Order("order_id", List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0,
                        LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), "delivery_address", "customer_name",
                        "customer_email", "customer_phone", false));
        when(mockOrderRepository.findById("order_id")).thenReturn(order);

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.findById("order_id");

        // Verify the results
    }

    @Test
    void testFindById_OrderRepositoryReturnsAbsent() {
        // Setup
        when(mockOrderRepository.findById("order_id")).thenReturn(Optional.empty());

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.findById("order_id");

        // Verify the results
    }

    @Test
    void testMakeOrder() {
        // Setup
        final Order order = new Order("order_id",
                List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0, LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 1), "delivery_address", "customer_name", "customer_email", "customer_phone",
                false);

        // Configure ProductRepository.findById(...).
        final Optional<Product> product = Optional.of(new Product("product_id", "name", "description", 0.0, 0));
        when(mockProductRepository.findById("product_id")).thenReturn(product);

        // Configure ProductRepository.saveAll(...).
        final List<Product> products = List.of(new Product("product_id", "name", "description", 0.0, 0));
        when(mockProductRepository.saveAll(
                List.of(new Product("product_id", "name", "description", 0.0, 0)))).thenReturn(products);

        // Configure OrderRepository.save(...).
        final Order order1 = new Order("order_id",
                List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0, LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 1), "delivery_address", "customer_name", "customer_email", "customer_phone",
                false);
        when(mockOrderRepository.save(
                new Order("order_id", List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0,
                        LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), "delivery_address", "customer_name",
                        "customer_email", "customer_phone", false))).thenReturn(order1);

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.makeOrder(order);

        // Verify the results
        verify(mockProductRepository).saveAll(List.of(new Product("product_id", "name", "description", 0.0, 0)));
        verify(mockOrderRepository).save(
                new Order("order_id", List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0,
                        LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), "delivery_address", "customer_name",
                        "customer_email", "customer_phone", false));
    }

    @Test
    void testMakeOrder_ProductRepositoryFindByIdReturnsAbsent() {
        // Setup
        final Order order = new Order("order_id",
                List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0, LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 1), "delivery_address", "customer_name", "customer_email", "customer_phone",
                false);
        when(mockProductRepository.findById("product_id")).thenReturn(Optional.empty());

        // Configure ProductRepository.saveAll(...).
        final List<Product> products = List.of(new Product("product_id", "name", "description", 0.0, 0));
        when(mockProductRepository.saveAll(
                List.of(new Product("product_id", "name", "description", 0.0, 0)))).thenReturn(products);

        // Configure OrderRepository.save(...).
        final Order order1 = new Order("order_id",
                List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0, LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 1), "delivery_address", "customer_name", "customer_email", "customer_phone",
                false);
        when(mockOrderRepository.save(
                new Order("order_id", List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0,
                        LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), "delivery_address", "customer_name",
                        "customer_email", "customer_phone", false))).thenReturn(order1);

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.makeOrder(order);

        // Verify the results
        verify(mockProductRepository).saveAll(List.of(new Product("product_id", "name", "description", 0.0, 0)));
        verify(mockOrderRepository).save(
                new Order("order_id", List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0,
                        LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), "delivery_address", "customer_name",
                        "customer_email", "customer_phone", false));
    }

    @Test
    void testMakeOrder_ProductRepositorySaveAllThrowsOptimisticLockingFailureException() {
        // Setup
        final Order order = new Order("order_id",
                List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0, LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 1), "delivery_address", "customer_name", "customer_email", "customer_phone",
                false);

        // Configure ProductRepository.findById(...).
        final Optional<Product> product = Optional.of(new Product("product_id", "name", "description", 0.0, 0));
        when(mockProductRepository.findById("product_id")).thenReturn(product);

        when(mockProductRepository.saveAll(
                List.of(new Product("product_id", "name", "description", 0.0, 0))))
                .thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.makeOrder(order);

        // Verify the results
    }

    @Test
    void testMakeOrder_OrderRepositoryThrowsOptimisticLockingFailureException() {
        // Setup
        final Order order = new Order("order_id",
                List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0, LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 1), "delivery_address", "customer_name", "customer_email", "customer_phone",
                false);

        // Configure ProductRepository.findById(...).
        final Optional<Product> product = Optional.of(new Product("product_id", "name", "description", 0.0, 0));
        when(mockProductRepository.findById("product_id")).thenReturn(product);

        // Configure ProductRepository.saveAll(...).
        final List<Product> products = List.of(new Product("product_id", "name", "description", 0.0, 0));
        when(mockProductRepository.saveAll(
                List.of(new Product("product_id", "name", "description", 0.0, 0)))).thenReturn(products);

        when(mockOrderRepository.save(
                new Order("order_id", List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0,
                        LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), "delivery_address", "customer_name",
                        "customer_email", "customer_phone", false))).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        final GenericResponseDTO result = orderServiceImplUnderTest.makeOrder(order);

        // Verify the results
        verify(mockProductRepository).saveAll(List.of(new Product("product_id", "name", "description", 0.0, 0)));
    }

    @Test
    void testUpdateProductQuantity() {
        // Setup
        final List<OrderProduct> productsToPurchase = List.of(
                new OrderProduct("product_id", "name", "description", 0.0, 0));

        // Configure ProductRepository.findById(...).
        final Optional<Product> product = Optional.of(new Product("product_id", "name", "description", 0.0, 0));
        when(mockProductRepository.findById("product_id")).thenReturn(product);

        // Configure ProductRepository.saveAll(...).
        final List<Product> products = List.of(new Product("product_id", "name", "description", 0.0, 0));
        when(mockProductRepository.saveAll(
                List.of(new Product("product_id", "name", "description", 0.0, 0)))).thenReturn(products);

        // Run the test
        orderServiceImplUnderTest.updateProductQuantity(productsToPurchase);

        // Verify the results
        verify(mockProductRepository).saveAll(List.of(new Product("product_id", "name", "description", 0.0, 0)));
    }

    @Test
    void testUpdateProductQuantity_ProductRepositoryFindByIdReturnsAbsent() {
        // Setup
        final List<OrderProduct> productsToPurchase = List.of(
                new OrderProduct("product_id", "name", "description", 0.0, 0));
        when(mockProductRepository.findById("product_id")).thenReturn(Optional.empty());

        // Configure ProductRepository.saveAll(...).
        final List<Product> products = List.of(new Product("product_id", "name", "description", 0.0, 0));
        when(mockProductRepository.saveAll(
                List.of(new Product("product_id", "name", "description", 0.0, 0)))).thenReturn(products);

        // Run the test
        orderServiceImplUnderTest.updateProductQuantity(productsToPurchase);

        // Verify the results
        verify(mockProductRepository).saveAll(List.of(new Product("product_id", "name", "description", 0.0, 0)));
    }

    @Test
    void testUpdateProductQuantity_ProductRepositorySaveAllThrowsOptimisticLockingFailureException() {
        // Setup
        final List<OrderProduct> productsToPurchase = List.of(
                new OrderProduct("product_id", "name", "description", 0.0, 0));

        // Configure ProductRepository.findById(...).
        final Optional<Product> product = Optional.of(new Product("product_id", "name", "description", 0.0, 0));
        when(mockProductRepository.findById("product_id")).thenReturn(product);

        when(mockProductRepository.saveAll(
                List.of(new Product("product_id", "name", "description", 0.0, 0))))
                .thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> orderServiceImplUnderTest.updateProductQuantity(productsToPurchase))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }
}
