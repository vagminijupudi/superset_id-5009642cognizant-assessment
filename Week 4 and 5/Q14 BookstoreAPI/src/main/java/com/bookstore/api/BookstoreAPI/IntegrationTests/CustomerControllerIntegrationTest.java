package com.bookstore.api.BookstoreAPI.IntegrationTests;

import com.bookstore.api.BookstoreAPI.Customer.Customer;
import com.bookstore.api.BookstoreAPI.Customer.CustomerController;
import com.bookstore.api.BookstoreAPI.Customer.CustomerDTO;
import com.bookstore.api.BookstoreAPI.Customer.CustomerRepository;
import com.bookstore.api.BookstoreAPI.ErrorHandling.ResourceNotFoundException;
import com.bookstore.api.BookstoreAPI.ErrorHandling.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@SpringBootTest
@ActiveProfiles("test")
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setup() {
        // No need to setup MockMvc here if using @SpringBootTest
    }

    @Test
    public void testCreateCustomer() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO(1L,"John Doe", "john@example.com","90605654504");
        String jsonContent = objectMapper.writeValueAsString(customerDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetCustomerById() throws Exception {
        Customer customer = new Customer("John Doe", "john@example.com","9000030240");
        CustomerDTO customerDTO = new CustomerDTO(customer.getId(), customer.getName(), customer.getEmail(),customer.getPhoneNumber());

        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@example.com"));
    }

    // Add more tests for other CRUD operations
}
