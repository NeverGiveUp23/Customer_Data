package com.felixvargas.customer.controller;

import com.felixvargas.customer.DTO.CustomerDTO;
import com.felixvargas.customer.model.Customer;
import com.felixvargas.customer.records.CustomerRegReq;
import com.felixvargas.customer.records.CustomerUpdateRequest;
import com.felixvargas.customer.service.CustomerService;
import com.felixvargas.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController  // Spring annotation indicating that this class is a REST controller
@RequestMapping("/api/v1/customer")  // Root URL for all customer-related endpoints
public class CustomerController {

    private final CustomerService customerService;  // Field to hold a CustomerService object

    private final JWTUtil jwtUtil;

    // Constructor that injects a CustomerJPADataAccessService and CustomerService object
    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        // Field to hold a CustomerJPADataAccessService object
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    // Endpoint to retrieve all customers
    @GetMapping
    public List<CustomerDTO> getCustomers() {
        return customerService.selectAllCustomers();  // Calls the selectAllCustomer() method of the
        // customerJPADataAccessService object
    }

    // Endpoint to retrieve a customer by ID (returns Optional)
    @GetMapping("{id}")
    public Optional<CustomerDTO> getCustomers(@PathVariable("id") Integer id) {
        return Optional.ofNullable(customerService.getCustomer(id));  // Calls the selectCustomerById() method of the
        // customerJPADataAccessService object
    }

    // Endpoint to add a new customer
    @PostMapping
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegReq request) {
        customerService.addCustomer(request);  // Calls the addCustomer() method of the customerService object
        String jwtToken = jwtUtil.issueToken(request.email(),"ROLE_USER"); // Issue a token for the new customer
        String message = "Customer %s Successfully Created".formatted(request.name());  // Create success message
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .body(message);  // Returns a success response with the message
//                .build();  // Returns a success response with the message
    }

    // Endpoint to delete a customer by ID
    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Integer customerId) {
        customerService.deleteCustomerById(customerId);  // Calls the deleteCustomerById() method of the customerService
        // object
    }

    // Endpoint to update a customer by ID
    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable("customerId") Integer customerId,
                               @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        customerService.updateCustomer(customerId, customerUpdateRequest);  // Calls the updateCustomer() method of the customerService object
    }


//     upload s3 image
    @PostMapping(
            value = "{id}/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadCustomerProfilePic(@PathVariable("id") Integer id,
                                         @RequestParam("file")MultipartFile multipartFile){

        customerService.uploadCustomerImage(id, multipartFile);

    }

    @GetMapping(
            value = "{customerId}/profile-image",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getCustomerProfilePicture(@PathVariable("customerId") Integer customerId){

       return customerService.getCustomerProfileImage(customerId);

    }
}
