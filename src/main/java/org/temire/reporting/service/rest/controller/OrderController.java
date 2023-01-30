package org.temire.reporting.service.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.temire.reporting.service.data.model.Order;
import org.temire.reporting.service.data.repositories.OrderRepository;
import org.temire.reporting.service.rest.response.GenericResponseDTO;
import org.temire.reporting.service.services.OrderService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/all")
    public ResponseEntity<GenericResponseDTO> all(Pageable pageable) {
        GenericResponseDTO genericResponseDTO = orderService.findAll(pageable);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @GetMapping("/id/{order_id}")
    public ResponseEntity<GenericResponseDTO> all(@PathVariable String order_id) {
        GenericResponseDTO genericResponseDTO = orderService.findById(order_id);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @GetMapping("/by-range/{start}/{end}")
    public ResponseEntity<GenericResponseDTO> byRange(Pageable pageable, @PathVariable LocalDate start, @PathVariable LocalDate end) {
        GenericResponseDTO response = orderService.byRange(pageable, start, end);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/by-range/{start}/{end}/{amount}")
    public ResponseEntity<GenericResponseDTO> byRange(Pageable pageable, @PathVariable LocalDate start, @PathVariable LocalDate end, @PathVariable double amount) {
        GenericResponseDTO response = orderService.byRange(pageable, start, end);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
