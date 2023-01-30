package org.temire.reporting.service.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.temire.reporting.service.data.model.Order;
import org.temire.reporting.service.kafka.KafkaConstants;
import org.temire.reporting.service.rest.response.GenericResponseDTO;
import org.temire.reporting.service.services.OrderService;

@Service
public class OrderConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    private final OrderService orderService;

    public OrderConsumer(OrderService orderService) {
        this.orderService = orderService;
    }


    @KafkaListener(topics = KafkaConstants.ORDER_TOPIC_NAME,
            groupId = KafkaConstants.GROUP_ID)
    public void consume(Order order){
        LOGGER.info(String.format("Message received -> %s", order));

        GenericResponseDTO response;
        try{
            response = orderService.makeOrder(order);
            LOGGER.info(String.format("Order Response Code: %s", response.getCode()));
            LOGGER.info(String.format("Order Response Message: %s", response.getMessage()));
        }catch(Exception ex){
            response = new GenericResponseDTO("99", HttpStatus.EXPECTATION_FAILED , "Error creating order!", ex.getMessage());
            LOGGER.info(String.format("Order Error Response Code: %s", response.getCode()));
            LOGGER.info(String.format("Order Error Response Message: %s", response.getMessage()));
        }
    }
}