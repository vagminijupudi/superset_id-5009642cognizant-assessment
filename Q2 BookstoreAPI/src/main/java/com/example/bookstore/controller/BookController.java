package com.example.bookstore.controller;

import com.example.bookstore.model.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private List<Book> books = new ArrayList<>();

    // Initialize some sample data
    public BookController() {
        books.add(new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 10.99, "9780743273565"));
        books.add(new Book(2L, "1984", "George Orwell", 8.99, "9780451524935"));
        books.add(new Book(3L, "To Kill a Mockingbird", "Harper Lee", 12.99, "9780061120084"));
    }

    // Handle GET requests to retrieve all books
    @GetMapping
    public List<Book> getAllBooks() {
        return books;
    }

    // Handle GET requests to retrieve a book by ID
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return books.stream().filter(book -> book.getId().equals(id)).findFirst().orElse(null);
    }

    // Handle POST requests to add a new book
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        books.add(book);
        return book;
    }

    // Handle PUT requests to update an existing book by ID
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book book = books.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
        if (book != null) {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPrice(updatedBook.getPrice());
            book.setIsbn(updatedBook.getIsbn());
        }
        return book;
    }

    // Handle DELETE requests to remove a book by ID
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }
}
