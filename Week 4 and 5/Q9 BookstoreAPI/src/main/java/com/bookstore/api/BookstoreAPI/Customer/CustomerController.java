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

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CustomerDTO>> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            CustomerDTO customerDTO = new CustomerDTO(
                customer.get().getId(),
                customer.get().getName(),
                customer.get().getEmail(),
                customer.get().getPhone()
            );

            // Add HATEOAS links
            EntityModel<CustomerDTO> resource = EntityModel.of(customerDTO);
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getCustomerById(id)).withSelfRel();
            Link allCustomersLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getAllCustomers()).withRel("all-customers");
            resource.add(selfLink, allCustomersLink);

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Custom-Header", "CustomValue");
            headers.add("X-Powered-By", "Spring Boot");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Customer not found with id " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<CustomerDTO>>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<EntityModel<CustomerDTO>> customerDTOs = customers.stream().map(customer -> {
            CustomerDTO dto = new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()
            );

            EntityModel<CustomerDTO> resource = EntityModel.of(dto);
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getCustomerById(customer.getId())).withSelfRel();
            resource.add(selfLink);

            return resource;
        }).collect(Collectors.toList());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(customerDTOs, headers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EntityModel<CustomerDTO>> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        if (customerDTO.getName() == null || customerDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Name must not be empty");
        }
        if (customerDTO.getEmail() == null || customerDTO.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email must not be empty");
        }

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());

        Customer createdCustomer = customerRepository.save(customer);

        CustomerDTO responseDTO = new CustomerDTO(
            createdCustomer.getId(),
            createdCustomer.getName(),
            createdCustomer.getEmail(),
            createdCustomer.getPhone()
        );

        // Add HATEOAS links
        EntityModel<CustomerDTO> resource = EntityModel.of(responseDTO);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getCustomerById(createdCustomer.getId())).withSelfRel();
        Link allCustomersLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getAllCustomers()).withRel("all-customers");
        resource.add(selfLink, allCustomersLink);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(resource, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CustomerDTO>> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id " + id);
        }
        if (customerDTO.getName() == null || customerDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Name must not be empty");
        }
        if (customerDTO.getEmail() == null || customerDTO.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email must not be empty");
        }

        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());

        Customer updatedCustomer = customerRepository.save(customer);

        CustomerDTO responseDTO = new CustomerDTO(
            updatedCustomer.getId(),
            updatedCustomer.getName(),
            updatedCustomer.getEmail(),
            updatedCustomer.getPhone()
        );

        // Add HATEOAS links
        EntityModel<CustomerDTO> resource = EntityModel.of(responseDTO);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getCustomerById(updatedCustomer.getId())).withSelfRel();
        Link allCustomersLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CustomerController.class).getAllCustomers()).withRel("all-customers");
        resource.add(selfLink, allCustomersLink);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "CustomValue");
        headers.add("X-Powered-By", "Spring Boot");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
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
