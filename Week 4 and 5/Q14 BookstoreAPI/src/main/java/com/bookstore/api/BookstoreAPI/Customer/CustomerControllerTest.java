package com.bookstore.api.BookstoreAPI.Customer;

import com.bookstore.api.BookstoreAPI.Customer.CustomerController;
import com.bookstore.api.BookstoreAPI.Customer.Customer;
import com.bookstore.api.BookstoreAPI.Customer.CustomerRepository;
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
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void whenValidCustomerId_thenCustomerShouldBeFound() throws Exception {
        Customer customer = new Customer("John Doe", "john.doe@example.com","9389243001");
        when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));

        mockMvc.perform(get("/customers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
}
