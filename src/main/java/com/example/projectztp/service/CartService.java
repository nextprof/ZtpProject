package com.example.projectztp.service;

import com.example.projectztp.domain.Book;
import com.example.projectztp.domain.BookRepository;
import com.example.projectztp.domain.Cart;
import com.example.projectztp.domain.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public Flux<Book> getCart() {
        return mongoTemplate.findOne(new Query().limit(1), Cart.class)
                .flatMapMany(cart -> Flux.fromIterable(cart.getBooks()));
    }

    public Flux<Book> addBookToCart(String bookId) {
        Mono<Cart> cartMono = mongoTemplate.findOne(new Query().limit(1), Cart.class);
        Mono<Book> bookMono = bookRepository.findById(bookId);

        return cartMono.zipWith(bookMono)
                .flatMap(tuple -> {
                    Cart cart = tuple.getT1();
                    Book book = tuple.getT2();

                    List<Book> books = cart.getBooks();
                    books.add(book);
                    cart.setBooks(books);
                    return cartRepository.save(cart);
                })
                .flatMapMany(savedCart -> Flux.fromIterable(savedCart.getBooks()));
    }

    public Flux<Book> deleteBookFromCart(String bookId) {
        Mono<Cart> cartMono = mongoTemplate.findOne(new Query().limit(1), Cart.class);
        return cartMono
                .flatMap(cart -> {
                    List<Book> books = cart.getBooks();
                    Iterator<Book> iterator = books.iterator();
//                    boolean bookFound = false;
                    while (iterator.hasNext()) {
                        Book next = iterator.next();
                        if (Objects.equals(next.getId(), bookId)) {
                            iterator.remove();
//                            bookFound = true;
                            break;
                        }
                    }
//                    if (!bookFound) {
//                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found in the cart"));
//                    }
                    cart.setBooks(books);
                    return cartRepository.save(cart);
                })
                .flatMapMany(savedCart -> Flux.fromIterable(savedCart.getBooks()));

    }
}
