package com.mani.commerce.orderservice.order.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Getter
@Table(name = "orders")
public class Order {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    //@Column(nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Version
    private long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Order() {
        // for JPA
    }

    public  Order(UUID id, OrderStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
      //  this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public UUID getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }


   public void setStatus() {
	   
   }
   
   public void confirm() {
	   
	   if(this.status != OrderStatus.CREATED) {
		   throw new IllegalStateException(
			   "Order can be confirmed only from CREATED state");
		   }
	   this.status=OrderStatus.CONFIRMED;
	   this.updatedAt= Instant.now();
	   }
   
   public void cancel() {
	   
	   if(this.status != OrderStatus.CREATED) {
		   throw new IllegalStateException("Order can be cancelled only from CREATED State");
	   }
	   
	   this.status = OrderStatus.CANCELLED;
	   this.updatedAt = Instant.now();
   }
}

