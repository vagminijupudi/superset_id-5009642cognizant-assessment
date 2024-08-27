package com.bookstore.api.BookstoreAPI.Book;

import com.bookstore.api.BookstoreAPI.ErrorHandling.ResourceNotFoundException;
import com.bookstore.api.BookstoreAPI.ErrorHandling.ValidationException;
import com.bookstore.api.BookstoreAPI.Metrics.BookMetrics; // Import the custom metrics class
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;
    private final BookMetrics bookMetrics; // Add BookMetrics

    public BookController(BookRepository bookRepository, BookMetrics bookMetrics) {
        this.bookRepository = bookRepository;
        this.bookMetrics = bookMetrics; // Initialize BookMetrics
    }

    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity<EntityModel<BookDTO>> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {
            BookDTO bookDTO = new BookDTO(
                book.get().getId(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getPrice(),
                book.get().getIsbn()
            );

            // Add HATEOAS links
            EntityModel<BookDTO> resource = EntityModel.of(bookDTO);
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).getBookById(id)).withSelfRel();
            Link allBooksLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).getAllBooks()).withRel("all-books");
            resource.add(selfLink, allBooksLink);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("X-Custom-Header", "CustomValue");
            responseHeaders.add("X-Powered-By", "Spring Boot");

            return new ResponseEntity<>(resource, responseHeaders, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
    }

    @GetMapping(produces = { "application/json", "application/xml" })
    public ResponseEntity<List<EntityModel<BookDTO>>> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        List<EntityModel<BookDTO>> bookDTOs = books.stream().map(book -> {
            BookDTO dto = new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getIsbn()
            );

            EntityModel<BookDTO> resource = EntityModel.of(dto);
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).getBookById(book.getId())).withSelfRel();
            resource.add(selfLink);

            return resource;
        }).collect(Collectors.toList());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Custom-Header", "CustomValue");
        responseHeaders.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(bookDTOs, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<EntityModel<BookDTO>> createBook(@Valid @RequestBody BookDTO bookDTO) {
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

        // Increment the custom metric
        bookMetrics.incrementBooksCreated();

        BookDTO responseDTO = new BookDTO(
            createdBook.getId(),
            createdBook.getTitle(),
            createdBook.getAuthor(),
            createdBook.getPrice(),
            createdBook.getIsbn()
        );

        // Add HATEOAS links
        EntityModel<BookDTO> resource = EntityModel.of(responseDTO);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).getBookById(createdBook.getId())).withSelfRel();
        Link allBooksLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).getAllBooks()).withRel("all-books");
        resource.add(selfLink, allBooksLink);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Custom-Header", "CustomValue");
        responseHeaders.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(resource, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<EntityModel<BookDTO>> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }

        if (bookDTO.getPrice() <= 0) {
            throw new ValidationException("Price must be greater than zero");
        }
        if (bookDTO.getTitle() == null || bookDTO.getTitle().trim().isEmpty()) {
            throw new ValidationException("Title must not be empty");
        }

        Book book = new Book();
        book.setId(id);
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPrice(bookDTO.getPrice());
        book.setIsbn(bookDTO.getIsbn());

        Book updatedBook = bookRepository.save(book);

        BookDTO responseDTO = new BookDTO(
            updatedBook.getId(),
            updatedBook.getTitle(),
            updatedBook.getAuthor(),
            updatedBook.getPrice(),
            updatedBook.getIsbn()
        );

        // Add HATEOAS links
        EntityModel<BookDTO> resource = EntityModel.of(responseDTO);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).getBookById(updatedBook.getId())).withSelfRel();
        Link allBooksLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).getAllBooks()).withRel("all-books");
        resource.add(selfLink, allBooksLink);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Custom-Header", "CustomValue");
        responseHeaders.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(resource, responseHeaders, HttpStatus.OK);
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
