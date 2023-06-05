package com.example.projectztp.controller;

import com.example.projectztp.domain.Order;
import com.example.projectztp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public Flux<Order> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{id}")
    public Mono<Order> getOrder(@PathVariable(value = "id") String id) {
        return orderService.getOrder(id);
    }

    @PostMapping("")
    public Mono<Order> createOrder() {
        return orderService.makeOrder();
    }
}
