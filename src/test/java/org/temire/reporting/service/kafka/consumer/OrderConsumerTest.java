package org.temire.reporting.service.kafka.consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.temire.reporting.service.data.dto.OrderProduct;
import org.temire.reporting.service.data.model.Order;
import org.temire.reporting.service.rest.response.GenericResponseDTO;
import org.temire.reporting.service.services.OrderService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderConsumerTest {

    @Mock
    private OrderService mockOrderService;

    private OrderConsumer orderConsumerUnderTest;

    @BeforeEach
    void setUp() {
        orderConsumerUnderTest = new OrderConsumer(mockOrderService);
    }

    @Test
    void testConsume() {
        // Setup
        final Order order = new Order("order_id",
                List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0, LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 1), "delivery_address", "customer_name", "customer_email", "customer_phone",
                false);

        // Configure OrderService.makeOrder(...).
        final GenericResponseDTO genericResponseDTO = new GenericResponseDTO("code", HttpStatus.OK, "message", "data",
                Map.ofEntries(Map.entry("value", "value")));
        when(mockOrderService.makeOrder(
                new Order("order_id", List.of(new OrderProduct("product_id", "name", "description", 0.0, 0)), 0.0,
                        LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), "delivery_address", "customer_name",
                        "customer_email", "customer_phone", false))).thenReturn(genericResponseDTO);

        // Run the test
        orderConsumerUnderTest.consume(order);

        // Verify the results
    }
}
