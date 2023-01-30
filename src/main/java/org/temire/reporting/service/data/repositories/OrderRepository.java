package org.temire.reporting.service.data.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.temire.reporting.service.data.model.Order;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    @Query("{'order_date': {$gte:?0, $lte:?1}}")
    List<Order> findByOrder_dateBetween(LocalDate from, LocalDate to, Pageable pageable);

    @Query("{'order_date': {$gte:?0, $lte:?1}, 'total_order_value':?2}")
    List<Order> findByOrder_dateBetweenAndAmount(LocalDate from, LocalDate to, double amount, Pageable pageable);

}