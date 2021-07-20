package com.cairone.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cairone.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
