package com.bookstore.api.BookstoreAPI.IntegrationTests;

import com.bookstore.api.BookstoreAPI.Book.Book;
import com.bookstore.api.BookstoreAPI.Book.BookDTO;
import com.bookstore.api.BookstoreAPI.Book.BookRepository;
import com.bookstore.api.BookstoreAPI.Metrics.BookMetrics;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookMetrics bookMetrics;

    @BeforeEach
    public void setup() {
        // No need to setup MockMvc here if using @SpringBootTest
    }

    @Test
    public void testCreateBook() throws Exception {
        BookDTO bookDTO = new BookDTO(null, "Title", "Author", 19.99, "1234567890");
        String jsonContent = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @SuppressWarnings("unused")
    @Test
    public void testGetBookById() throws Exception {
        Book book = new Book("Title", "Author", 19.99, "1234567890");
        BookDTO bookDTO = new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getIsbn());

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Author"));
    }

    // Add more tests for other CRUD operations
}
