package com.bookstore.api.BookstoreAPI.IntegrationTests;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.bookstore.api.BookstoreAPI.Book.BookController;
import com.bookstore.api.BookstoreAPI.Customer.CustomerController;

@Configuration
@Import({BookController.class, CustomerController.class})
public class TestConfig {
   
}
