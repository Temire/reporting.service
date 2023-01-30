package org.temire.reporting.service.services;

import org.springframework.data.domain.Pageable;
import org.temire.reporting.service.data.model.Order;
import org.temire.reporting.service.rest.response.GenericResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    GenericResponseDTO findAll(Pageable pageable);

    GenericResponseDTO byRange(Pageable pageable, LocalDate start, LocalDate end);

    GenericResponseDTO byRangeAndAmount(Pageable pageable, LocalDate start, LocalDate end, double amount);

    GenericResponseDTO findById(String order_id);

    GenericResponseDTO makeOrder(Order order);
}
