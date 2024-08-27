package com.bookstore.api.BookstoreAPI.Book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookDTO {

    private Long id;

    @NotBlank(message = "Title must not be empty")
    private String title;

    @NotBlank(message = "Author must not be empty")
    private String author;

    @NotNull(message = "Price must not be null")
    @Min(value = 0, message = "Price must be greater than or equal to zero")
    private Double price;

    @NotBlank(message = "ISBN must not be empty")
    private String isbn;

    public BookDTO() {
    }

    public BookDTO(Long id, String title, String author, Double price, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
