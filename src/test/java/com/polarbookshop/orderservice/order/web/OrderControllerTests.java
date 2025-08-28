package com.polarbookshop.orderservice.order.web;

import com.polarbookshop.orderservice.book.Book;
import com.polarbookshop.orderservice.order.domain.Order;
import com.polarbookshop.orderservice.order.domain.OrderService;
import com.polarbookshop.orderservice.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.BDDAssertions.then;

@WebFluxTest(OrderController.class)
class OrderControllerTests {
    @Autowired
    private WebTestClient webClient;
    
    @MockitoBean
    private OrderService orderService;
    
    @Test
    void whenBookNotAvailableThenRejectOrder() {
        var orderRequest = new OrderRequest("1234567890", 3);
        var expectedOrder = OrderService.buildRejectedOrder(orderRequest.isbn(), orderRequest.quantity());
        
        // Given
        given(orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity()))
                .willReturn(Mono.just(expectedOrder));
        
        // When
        webClient
                .post()
                .uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).value(actualOrder -> {
                    // Then
                    then(actualOrder).isNotNull();
                    then(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
                });
    }
    
    @Test
    void whenBookAvailableThenAccptedOder() {
        var orderRequest = new OrderRequest("1234567890", 3);
        
        var expectedBook = new Book(orderRequest.isbn(), "Title", "Author", 9.90);
        var expectedOrder = OrderService.buildAcceptedOrder(expectedBook, orderRequest.quantity());
        
        // Given
        given(orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity()))
                .willReturn(Mono.just(expectedOrder));
        
        // When
        webClient
                .post()
                .uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).value(actualOrder -> {
                    // Then
                    then(actualOrder).isNotNull();
                    then(actualOrder.status()).isEqualTo(OrderStatus.ACCEPTED);
                });
    }

}