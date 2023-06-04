package com.example.projectztp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Objects;

@Document
public class Book {

    @Id
    private String id;
    private String title;
    private String author;
    private LocalDate issuedAt;
    private String isbnNumber;

    public Book() {
    }

//    public boolean update(Book book) {
//        if (
//                !Objects.equals(this.title, book.getTitle()) ||
//                        !Objects.equals(this.author, book.getAuthor()) ||
//                        !Objects.equals(this.issuedAt, book.getIssuedAt()) ||
//                        !Objects.equals(this.isbnNumber, book.getIsbnNumber())
//        ) {
//            this.title = book.getTitle();
//            this.author = book.getAuthor();
//            this.issuedAt = book.getIssuedAt();
//            this.isbnNumber = book.getIsbnNumber();
//            return true;
//        }
//        return false;
//    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getIssuedAt() {
        return issuedAt;
    }

    public String getIsbnNumber() {
        return isbnNumber;
    }

    public void setId(String id) {
        this.id = id;
    }
}
