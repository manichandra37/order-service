package com.mani.commerce.orderservice.order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mani.commerce.orderservice.order.domain.Order;
import com.mani.commerce.orderservice.order.domain.OrderStatus;


@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class OrderRepositoryIT {
	
	@Autowired
	OrderRepository repository;
		
	@Test
	void SaveAndLoadOrder() {
		Order order = new Order(UUID.randomUUID(),OrderStatus.CREATED, Instant.now(),Instant.now());
	
	
	repository.save(order);
	
	Order loaded = repository.findById(order.getId()).orElseThrow();

    assertThat(loaded.getStatus()).isEqualTo("CREATED");
	}
	
	@Test
	void confirmOrder() {
		Order order = new Order(UUID.randomUUID(),OrderStatus.CREATED, Instant.now(),Instant.now());
	
	
	repository.save(order);
	
	Order loaded = repository.findById(order.getId()).orElseThrow();

    assertThat(loaded.getStatus()).isEqualTo("CREATED");
	}

}
