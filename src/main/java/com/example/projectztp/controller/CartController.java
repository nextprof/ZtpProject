package com.example.projectztp.controller;

import com.example.projectztp.domain.Book;
import com.example.projectztp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService service;

    @GetMapping
    public Flux<Book> getCart() {
        return service.getCart();
    }

    @PostMapping
    public Flux<Book> addBookToCart(@RequestBody BookIdDto dto) {
        return service.addBookToCart(dto.bookId());
    }

    @DeleteMapping
    public Flux<Book> deleteBookFromCart(@RequestBody BookIdDto dto) {
        return service.deleteBookFromCart(dto.bookId());
    }

}
