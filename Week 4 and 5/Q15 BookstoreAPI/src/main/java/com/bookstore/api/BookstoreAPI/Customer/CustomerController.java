package com.bookstore.api.BookstoreAPI.Customer;

import com.bookstore.api.BookstoreAPI.ErrorHandling.ResourceNotFoundException;
import com.bookstore.api.BookstoreAPI.ErrorHandling.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Operation(summary = "Get a customer by ID", description = "Retrieve a customer's details by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(
            @Parameter(description = "ID of the customer to be retrieved") @PathVariable Long id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            CustomerDTO customerDTO = convertToDTO(customer.get());
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Custom-Header", "CustomValue");
            headers.add("X-Powered-By", "Spring Boot");
            return new ResponseEntity<>(customerDTO, headers, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Customer with ID " + id + " not found");
        }
    }

    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    @ApiResponse(responseCode = "200", description = "List of customers")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOs = convertToDTOs(customers);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Powered-By", "Spring Boot");
        return new ResponseEntity<>(customerDTOs, headers, HttpStatus.OK);
    }

    @Operation(summary = "Create a new customer", description = "Add a new customer to the store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(
            @Parameter(description = "Customer details to be created") @Valid @RequestBody CustomerDTO customerDTO) {
        if (customerDTO.getName() == null || customerDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Name must not be empty");
        }

        Customer customer = convertToEntity(customerDTO);
        Customer createdCustomer = customerRepository.save(customer);
        CustomerDTO createdCustomerDTO = convertToDTO(createdCustomer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Powered-By", "Spring Boot");
        return new ResponseEntity<>(createdCustomerDTO, headers, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a customer", description = "Update the details of an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "ID of the customer to be updated") @PathVariable Long id,
            @Parameter(description = "Updated customer details") @Valid @RequestBody CustomerDTO customerDTO) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer with ID " + id + " not found");
        }

        Customer customer = convertToEntity(customerDTO);
        customer.setId(id);
        Customer updatedCustomer = customerRepository.save(customer);
        CustomerDTO updatedCustomerDTO = convertToDTO(updatedCustomer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Powered-By", "Spring Boot");
        return new ResponseEntity<>(updatedCustomerDTO, headers, HttpStatus.OK);
    }

    @Operation(summary = "Delete a customer", description = "Remove a customer from the store by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of the customer to be deleted") @PathVariable Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Custom-Header", "CustomValue");
            headers.add("X-Powered-By", "Spring Boot");
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("Customer with ID " + id + " not found");
        }
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());
        return customerDTO;
    }

    private List<CustomerDTO> convertToDTOs(List<Customer> customers) {
        return customers.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        return customer;
    }
}
