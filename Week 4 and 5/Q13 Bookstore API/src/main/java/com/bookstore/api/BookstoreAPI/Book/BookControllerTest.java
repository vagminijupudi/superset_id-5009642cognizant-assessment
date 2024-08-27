package com.bookstore.api.BookstoreAPI.Book;

import com.bookstore.api.BookstoreAPI.Book.BookController;
import com.bookstore.api.BookstoreAPI.Book.Book;
import com.bookstore.api.BookstoreAPI.Book.BookRepository;
import com.bookstore.api.BookstoreAPI.Book.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SuppressWarnings("unused")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void whenValidBookId_thenBookShouldBeFound() throws Exception {
        Book book = new Book("Hell", "Author 1", 29.99, "979-5678912");
        when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(book));

        mockMvc.perform(get("/books/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }
}