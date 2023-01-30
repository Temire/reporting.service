package org.temire.reporting.service.services.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.temire.reporting.service.CustomException;
import org.temire.reporting.service.data.dto.CheckedProduct;
import org.temire.reporting.service.data.dto.OrderProduct;
import org.temire.reporting.service.data.model.Order;
import org.temire.reporting.service.data.model.Product;
import org.temire.reporting.service.data.repositories.OrderRepository;
import org.temire.reporting.service.data.repositories.ProductRepository;
import org.temire.reporting.service.rest.response.GenericResponseDTO;
import org.temire.reporting.service.services.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderServiceImpl  implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private Lock lockForUpdate = new ReentrantLock();

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public GenericResponseDTO findAll(Pageable pageable) {
        GenericResponseDTO genericResponseDTO = new GenericResponseDTO("00", HttpStatus.OK, "Search Completed", orderRepository.findAll(pageable));
        return genericResponseDTO;
    }

    @Override
    public GenericResponseDTO byRange(Pageable pageable, LocalDate start, LocalDate end) {
        List<Order> range =  orderRepository.findByOrder_dateBetween(start, end, pageable);
        if(!range.isEmpty()) return new GenericResponseDTO("00", HttpStatus.OK, "Orders return successfully!", range);
        else return new GenericResponseDTO("11", HttpStatus.NO_CONTENT, "NO Orders AVAILABLE!", range);
    }

    public GenericResponseDTO byRangeAndAmount(Pageable pageable, LocalDate start, LocalDate end, double amount) {
        List<Order> range =  orderRepository.findByOrder_dateBetweenAndAmount(start, end, amount, pageable);
        if(!range.isEmpty()) return new GenericResponseDTO("00", HttpStatus.OK, "Orders return successfully!", range);
        else return new GenericResponseDTO("11", HttpStatus.NO_CONTENT, "NO Orders AVAILABLE!", range);
    }

    @Override
    public GenericResponseDTO findById(String order_id) {
        Optional<Order> order = orderRepository.findById(order_id);
        if(order.isPresent())
            return new GenericResponseDTO("00", HttpStatus.OK, "Order returned successfully!", order.get());
        else return new GenericResponseDTO("99", HttpStatus.NOT_FOUND, "Not Found", null);
    }

    @Override
    public GenericResponseDTO makeOrder(Order order) {
        try{
            updateProductQuantity(order.getItems());
            orderRepository.save(order);
            return new GenericResponseDTO("00", HttpStatus.OK, "Order placed successfully!", order);
        }catch(Exception ex){
            if(ex instanceof CustomException) {
                CustomException e = (CustomException) ex;
                return new GenericResponseDTO("99", HttpStatus.EXPECTATION_FAILED, e.getMessage(), e.getProductExceedingPurchassbleQTY());
            }
            else
                return new GenericResponseDTO("99", HttpStatus.EXPECTATION_FAILED, ex.getMessage(), null);

        }
    }

    public void updateProductQuantity(List<OrderProduct> productsToPurchase) throws CustomException {
        lockForUpdate.lock();
        List<Product> forOrder = new ArrayList<>();
        List<CheckedProduct> checkedProducts1 = new ArrayList<>();

        for(OrderProduct productDTO : productsToPurchase) {
            Optional<Product> optionalProduct = productRepository.findById(productDTO.getProduct_id());
            if (optionalProduct.isPresent()) {
                Product p = optionalProduct.get();
                int current_qty = p.getQuantity();
                if (current_qty > productDTO.getQuantity()) {
                    int new_qty = current_qty - productDTO.getQuantity();
                    p.setQuantity(new_qty);
                    forOrder.add(p);
                }
                else
                    checkedProducts1.add(new CheckedProduct("",p.getQuantity(), current_qty, false));
            }
        }
        if(checkedProducts1.isEmpty())
            productRepository.saveAll(forOrder);
        else
            throw new CustomException("Error: Purchase quantity of product(s) is higher than available product(s) quantity", checkedProducts1);
        lockForUpdate.unlock();
    }

}
