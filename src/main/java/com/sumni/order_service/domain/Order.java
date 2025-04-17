package com.sumni.order_service.domain;

import com.sumni.order_service.domain.enums.OrderStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("orders")
public record Order(
        @Id
        UUID id,
        String bookIsbn,
        String bookName,
        Double bookPrice,
        Integer quantity,
        OrderStatus status,
        @CreatedDate
        Instant createdDate,
        
        @LastModifiedDate
        Instant lastModifiedDate,
        
        @Version
        int version
) {
    public static Order of(String isbn, String bookName, Double bookPrice, Integer quantity, OrderStatus status) {
        return new Order(null, isbn, bookName, bookPrice, quantity, status, null, null, 0);
    }
    
    public static Order buildRejectedOrder(String isbn, int quantity) {
        return Order.of(isbn, null, null, quantity, OrderStatus.REJECTED);
    }
    
}
