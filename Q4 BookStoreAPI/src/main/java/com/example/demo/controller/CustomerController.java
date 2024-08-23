package com.example.demo.controller;

import com.example.demo.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    // Step 4: POST Endpoint to Create a New Customer (JSON Request Body)
    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        // Simulate saving the customer to a database
        customer.setId(1L); // Simulating database ID generation
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    // Step 5: POST Endpoint to Process Form Data for Customer Registration
    @PostMapping("/register")
    public ResponseEntity<Customer> registerCustomer(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("address") String address) {
        
        Customer customer = new Customer();
        customer.setId(2L); // Simulating database ID generation
        customer.setName(name);
        customer.setEmail(email);
        customer.setAddress(address);
        
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }
}
