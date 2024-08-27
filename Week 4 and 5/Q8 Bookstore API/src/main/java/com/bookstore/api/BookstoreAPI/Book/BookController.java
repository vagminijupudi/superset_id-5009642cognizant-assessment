package com.bookstore.api.BookstoreAPI.Book;

import com.bookstore.api.BookstoreAPI.ErrorHandling.ResourceNotFoundException;
import com.bookstore.api.BookstoreAPI.ErrorHandling.ValidationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Custom-Header", "CustomValue");
            headers.add("X-Powered-By", "Spring Boot");

            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(book.get().getId());
            bookDTO.setTitle(book.get().getTitle());
            bookDTO.setAuthor(book.get().getAuthor());
            bookDTO.setPrice(book.get().getPrice());
            bookDTO.setIsbn(book.get().getIsbn());

            return new ResponseEntity<>(bookDTO, headers, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        List<BookDTO> bookDTOs = books.stream().map(book -> {
            BookDTO dto = new BookDTO();
            dto.setId(book.getId());
            dto.setTitle(book.getTitle());
            dto.setAuthor(book.getAuthor());
            dto.setPrice(book.getPrice());
            dto.setIsbn(book.getIsbn());
            return dto;
        }).toList();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(bookDTOs, headers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        if (bookDTO.getPrice() <= 0) {
            throw new ValidationException("Price must be greater than zero");
        }
        if (bookDTO.getTitle() == null || bookDTO.getTitle().trim().isEmpty()) {
            throw new ValidationException("Title must not be empty");
        }

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPrice(bookDTO.getPrice());
        book.setIsbn(bookDTO.getIsbn());

        Book createdBook = bookRepository.save(book);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Powered-By", "Spring Boot");

        BookDTO responseDTO = new BookDTO();
        responseDTO.setId(createdBook.getId());
        responseDTO.setTitle(createdBook.getTitle());
        responseDTO.setAuthor(createdBook.getAuthor());
        responseDTO.setPrice(createdBook.getPrice());
        responseDTO.setIsbn(createdBook.getIsbn());

        return new ResponseEntity<>(responseDTO, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }

        if (bookDTO.getPrice() <= 0) {
            throw new ValidationException("Price must be greater than zero");
        }

        Book book = new Book();
        book.setId(id);
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPrice(bookDTO.getPrice());
        book.setIsbn(bookDTO.getIsbn());

        Book updatedBook = bookRepository.save(book);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Powered-By", "Spring Boot");

        BookDTO responseDTO = new BookDTO();
        responseDTO.setId(updatedBook.getId());
        responseDTO.setTitle(updatedBook.getTitle());
        responseDTO.setAuthor(updatedBook.getAuthor());
        responseDTO.setPrice(updatedBook.getPrice());
        responseDTO.setIsbn(updatedBook.getIsbn());

        return new ResponseEntity<>(responseDTO, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }

        bookRepository.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
