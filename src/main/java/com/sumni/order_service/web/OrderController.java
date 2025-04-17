package com.sumni.order_service.web;

import com.sumni.order_service.domain.Order;
import com.sumni.order_service.domain.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping
    public Flux<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    @PostMapping
    public Mono<Order> submitOrder(@RequestBody @Valid OrderRequest orderRequest) {
        return orderService.submitOrder(
                orderRequest.isbn(),
                orderRequest.quantity()
        );
    }
}
