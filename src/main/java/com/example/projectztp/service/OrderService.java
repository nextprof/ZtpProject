package com.example.projectztp.service;

import com.example.projectztp.domain.Cart;
import com.example.projectztp.domain.Order;
import com.example.projectztp.domain.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;


    public Flux<Order> getOrders() {
        return repository.findAll();
    }

    public Mono<Order> getOrder(String orderId) {
        return repository.findById(orderId);
    }

    public Mono<Order> makeOrder() {

        return Mono.from(reactiveMongoTemplate.execute(transactionalOperator -> {

            Mono<Cart> cartMono = reactiveMongoTemplate.findOne(new Query().limit(1), Cart.class)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found")));

            Mono<Order> orderMono = cartMono.flatMap(cart -> {
                Order order = new Order();
                order.setBooks(new ArrayList<>(cart.getBooks()));
                return reactiveMongoTemplate.insert(order);
            });

            Mono<Cart> updatedCartMono = cartMono.flatMap(cart -> {
                cart.setBooks(Collections.emptyList());
                return reactiveMongoTemplate.save(cart);
            });

            return Mono.zip(orderMono, updatedCartMono)
                    .map(Tuple2::getT1);
        }));
    }

}
