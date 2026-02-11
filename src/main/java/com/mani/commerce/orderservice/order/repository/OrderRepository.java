package com.mani.commerce.orderservice.order.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mani.commerce.orderservice.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, UUID>{
	

}
