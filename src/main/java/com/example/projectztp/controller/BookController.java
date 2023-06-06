package com.example.projectztp.controller;

import com.example.projectztp.domain.Book;
import com.example.projectztp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Book> getAllBooks(@RequestParam(name = "author", required = false) String author,
                                  @RequestParam(name = "year", required = false) Integer year) {
        if(author != null && year != null) {
            return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only one filter parameter available"));
        }
        return bookService.getBooks(author,year);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Book> getSingleBook(@PathVariable("id") String id) {
        return bookService.getBook(id);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Book> createBook(@RequestBody Mono<Book> book) {
        return bookService.saveBook(book);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Book> updateBook(@RequestBody Mono<Book> book, @PathVariable("id") String id) {
        return bookService.updateBook(book, id);
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> deleteBook(@PathVariable(value = "id") String id) {
        return bookService.deleteBook(id);
    }

}
