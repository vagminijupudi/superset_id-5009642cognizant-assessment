package com.example.demo.controller;

import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks().stream()
                .map(BookMapper.INSTANCE::bookToBookDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(BookMapper.INSTANCE.bookToBookDTO(book));
    }

    @PostMapping
    public BookDTO createBook(@RequestBody BookDTO bookDTO) {
        Book book = BookMapper.INSTANCE.bookDTOToBook(bookDTO);
        return BookMapper.INSTANCE.bookToBookDTO(bookService.createBook(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        Book book = BookMapper.INSTANCE.bookDTOToBook(bookDTO);
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(BookMapper.INSTANCE.bookToBookDTO(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
