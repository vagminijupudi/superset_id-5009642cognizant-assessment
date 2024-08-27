package com.bookstore.api.BookstoreAPI.Customer;

import com.bookstore.api.BookstoreAPI.ErrorHandling.ResourceNotFoundException;
import com.bookstore.api.BookstoreAPI.ErrorHandling.ValidationException;
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
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity<EntityModel<CustomerDTO>> getCustomerById(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            CustomerDTO customerDTO = new CustomerDTO(
                customer.get().getId(),
                customer.get().getName(),
                customer.get().getEmail(),
                customer.get().getPhoneNumber()
            );

            // Add HATEOAS links
            EntityModel<CustomerDTO> resource = EntityModel.of(customerDTO);
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getCustomerById(id, headers)).withSelfRel();
            Link allCustomersLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getAllCustomers(headers)).withRel("all-customers");
            resource.add(selfLink, allCustomersLink);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("X-Custom-Header", "CustomValue");
            responseHeaders.add("X-Powered-By", "Spring Boot");

            return new ResponseEntity<>(resource, responseHeaders, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Customer not found with id " + id);
        }
    }

    @GetMapping(produces = { "application/json", "application/xml" })
    public ResponseEntity<List<EntityModel<CustomerDTO>>> getAllCustomers(@RequestHeader HttpHeaders headers) {
        List<Customer> customers = customerRepository.findAll();

        List<EntityModel<CustomerDTO>> customerDTOs = customers.stream().map(customer -> {
            CustomerDTO dto = new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber()
            );

            EntityModel<CustomerDTO> resource = EntityModel.of(dto);
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getCustomerById(customer.getId(), headers)).withSelfRel();
            resource.add(selfLink);

            return resource;
        }).collect(Collectors.toList());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Custom-Header", "CustomValue");
        responseHeaders.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(customerDTOs, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<EntityModel<CustomerDTO>> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        if (customerDTO.getEmail() == null || !customerDTO.getEmail().contains("@")) {
            throw new ValidationException("Email must be valid");
        }
        if (customerDTO.getName() == null || customerDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Name must not be empty");
        }

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());

        Customer createdCustomer = customerRepository.save(customer);

        CustomerDTO responseDTO = new CustomerDTO(
            createdCustomer.getId(),
            createdCustomer.getName(),
            createdCustomer.getEmail(),
            createdCustomer.getPhoneNumber()
        );

        // Add HATEOAS links
        EntityModel<CustomerDTO> resource = EntityModel.of(responseDTO);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getCustomerById(createdCustomer.getId(), new HttpHeaders())).withSelfRel();
        Link allCustomersLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getAllCustomers(new HttpHeaders())).withRel("all-customers");
        resource.add(selfLink, allCustomersLink);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Custom-Header", "CustomValue");
        responseHeaders.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(resource, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<EntityModel<CustomerDTO>> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id " + id);
        }

        if (customerDTO.getEmail() == null || !customerDTO.getEmail().contains("@")) {
            throw new ValidationException("Email must be valid");
        }
        if (customerDTO.getName() == null || customerDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Name must not be empty");
        }

        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());

        Customer updatedCustomer = customerRepository.save(customer);

        CustomerDTO responseDTO = new CustomerDTO(
            updatedCustomer.getId(),
            updatedCustomer.getName(),
            updatedCustomer.getEmail(),
            updatedCustomer.getPhoneNumber()
        );

        // Add HATEOAS links
        EntityModel<CustomerDTO> resource = EntityModel.of(responseDTO);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getCustomerById(updatedCustomer.getId(), new HttpHeaders())).withSelfRel();
        Link allCustomersLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getAllCustomers(new HttpHeaders())).withRel("all-customers");
        resource.add(selfLink, allCustomersLink);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Custom-Header", "CustomValue");
        responseHeaders.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(resource, responseHeaders, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id " + id);
        }

        customerRepository.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
