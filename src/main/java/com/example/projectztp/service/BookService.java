package com.example.projectztp.service;

import com.example.projectztp.domain.Book;
import com.example.projectztp.domain.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public Flux<Book> getBooks(String author, Integer year) {

        if (author != null) {
            Query query = Query.query(Criteria.where("author").is(author));
            return mongoTemplate.find(query, Book.class);
        }

        if (year != null) {
            ProjectionOperation projectOperation = Aggregation.project("title", "author", "issuedAt", "isbnNumber")
                    .andExpression("year(issuedAt)").as("year");

            MatchOperation matchOperation = Aggregation.match(Criteria.where("year").is(year));

            Aggregation aggregation = Aggregation.newAggregation(projectOperation, matchOperation);

            return mongoTemplate.aggregate(aggregation, "book", Book.class);
        }

        return bookRepository.findAll();
    }

    public Mono<Book> getBook(String id) {
        return bookRepository.findById(id);
    }

    public Mono<Book> saveBook(Mono<Book> book) {
        return book.flatMap(bookRepository::insert);
    }

    public Mono<Book> updateBook(Mono<Book> book, String id) {
        return bookRepository.findById(id).flatMap(b -> book)
                .doOnNext(updated -> updated.setId(id))
                .flatMap(bookRepository::save);
    }

    public Mono<Void> deleteBook(String id) {
        return bookRepository.deleteById(id);
    }
}
