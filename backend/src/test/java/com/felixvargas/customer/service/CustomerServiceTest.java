package com.felixvargas.customer.service;

import com.felixvargas.customer.DTO.CustomerDTO;
import com.felixvargas.customer.DTO.CustomerDTOMapper;
import com.felixvargas.customer.interfaces.CustomerDAO;
import com.felixvargas.customer.model.Customer;
import com.felixvargas.customer.enums.Gender;
import com.felixvargas.customer.records.CustomerRegReq;
import com.felixvargas.customer.records.CustomerUpdateRequest;
import com.felixvargas.exception.DuplicateResourceException;
import com.felixvargas.exception.RequestValidationException;
import com.felixvargas.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
// JUnit 5 extension that allows the use of Mockito annotations in tests
class CustomerServiceTest {

    private CustomerService customerServiceTest;
    // The object being tested

    private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();
    @Mock
    // Annotation that creates a mock object to use in place of the actual object
    private CustomerDAO customerDAO;
    // Mocked object that the CustomerService object depends on
    @Mock
    private PasswordEncoder passwordEncoder;




    @BeforeEach
        // Annotation that indicates a method to be run before each test method
    void setUp() {
        customerServiceTest = new CustomerService(customerDAO, passwordEncoder, customerDTOMapper);
        // Create a new instance of CustomerService with the mocked CustomerDAO object injected
    }

    @Test
    void selectAllCustomers() {
        // Call the selectAllCustomers() method of the test object underTest
        customerServiceTest.selectAllCustomers();
        // Then verify that the selectAllCustomer() method of the injected mock CustomerDAO object is called
        verify(customerDAO).selectAllCustomer();
    }

    @Test
    void canSelectCustomerById() {
        // Given
        int id = 100;
        Customer customer = new Customer(
                "felix", "felix@aol.com", "password", 22,
                Gender.MALE);

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerDTO expected = customerDTOMapper.apply(customer);
        //WHEN
        CustomerDTO actual = customerServiceTest.getCustomer(id);
        // Then

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willThrowWhenSelectCustomerByIdNotFound() {
        // Given
        int id = 100;

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> customerServiceTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "Customer with id %s not found".formatted(id)
                );

    }

    @Test
    void addCustomer() {
        // Given
        String email = "Felix@aol.com";

        when(customerDAO.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegReq customerRegReq = new CustomerRegReq(
                "felix", email, "password", 22, Gender.MALE
        );


        when(passwordEncoder.encode(customerRegReq.password())).thenReturn("");
        // When
        String passwordHash = "kcndkfnd78";
        when(passwordEncoder.encode(customerRegReq.password())).thenReturn(passwordHash);
        customerServiceTest.addCustomer(customerRegReq);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customerRegReq.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerRegReq.email());
        assertThat(capturedCustomer.getPassword()).isEqualTo(passwordHash);
        assertThat(capturedCustomer.getAge()).isEqualTo(customerRegReq.age());
    }

    @Test
    void WillThrowWhenEmailExistWhileAddingCustomer() {
        // Given
        // create a mock email
        String email = "Felix@aol.com";
        // create a new customer with the registration request
        CustomerRegReq customerRegReq = new CustomerRegReq(
                "felix", email, "password", 22, Gender.MALE
        );
        // now with Mockito checking if the customer with the given email exist -> return true
        // because in this test we want to throw the exception
        when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);

        // When
        // assertion that the addCustomer() method should throw an exception when email exist.
        assertThatThrownBy(() -> customerServiceTest.addCustomer(customerRegReq))
                // create an instance of the DuplicateResourceException class
                .isInstanceOf(DuplicateResourceException.class)
                // throw the certain error message in the addCustomer() method
                .hasMessage(
                        "email already taken"
                );

        // Then
        verify(customerDAO, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        // Given
        // create an id
        int id = 1;
        // checking if the person with id exist in the database -> return true because in this test it does
        when(customerDAO.existsPersonWithId(id)).thenReturn(true);
        // When
        // now delete customer by the id
        customerServiceTest.deleteCustomerById(id);
        // Then
        // verify using mockito to check if the customer has been deleted
        verify(customerDAO).deleteCustomerById(id);
    }

    @Test
    void WillThrowDeleteCustomerByIdWhenNotFound() {
        // Given
        int id = 1;

        when(customerDAO.existsPersonWithId(id)).thenReturn(false);
        // When
        // assertion that the deleteCustomerById() method should throw an exception.
        assertThatThrownBy(() -> customerServiceTest.deleteCustomerById(id))
                // make an instance of the ResourceNotFoundException class
                .isInstanceOf(ResourceNotFoundException.class)
                // throw the certain error message in the CustomerService class
                .hasMessage(
                        "Customer with id %s not found".formatted(id)
                );
        // Then
        // verify that the deleteCustomerById() method will not be called
        verify(customerDAO, never()).deleteCustomerById(id);
    }

    @Test
    void updateCustomerAllCustomerProperties() {
        // Given
        // create an id
        int id = 10;
        // create a new customer
        Customer customer = new Customer(
                "felix", "felix@aol.com", "password", 22,
                Gender.MALE);
        // use mockito to select the customer using the CustomerDAO instance
        // return optional object containing the customer object just created
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // create an updated email
        String newEmail = "felix123@aol.com";
        // create the updated customer using the CustomerUpdateRequest record -> passing in the newEmail
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                "Andy", newEmail, 25
        );
        // use Mockito to check if the email that is new already exist in the database -> set to false since we are
        // checking to make sure that we can update the customer
        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(false);
        // When
        // use the customerServiceTest instance to update the customer -> pass in the two args -> the id we created
        // and the new updated customer
        customerServiceTest.updateCustomer(id, customerUpdateRequest);

        // Then
        // create an ArgumentCapture object to capture the arg passed to the updateCustomer() method.
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        // use Mockito to verify that the CustomerDAO object was called with the arg captured by ArgumentCaptor.
        verify(customerDAO).updateCustomer(argumentCaptor.capture());
        // grabs the value that was captured and assign it to capturedCustomer.
        Customer capturedCustomer = argumentCaptor.getValue();
        // these assertions ensure that the customer object was updated
        assertThat(capturedCustomer.getName()).isEqualTo(customerUpdateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerUpdateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerUpdateRequest.age());
    }

    @Test
    void updateCustomerCustomerName() {
        // GIVEN
        // create an id
        int id = 10;
        //create a new customer
        Customer customer = new Customer(
                "felix", "felix@aol.com", "password", 22,
                Gender.MALE);
        // use mockito to select the customer using the CustomerDAO instance
        // return optional object containing the customer object just created
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //create an update request with only the name -> null for email + age indicates only the name should be updated
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                "Andy", null, null
        );
        // WHEN
        // call the updateCustomer method with the id and customerUpdateRequest arguments
        customerServiceTest.updateCustomer(id, customerUpdateRequest);

        // THEN
        // // create an ArgumentCapture object to capture the arg passed to the updateCustomer() method.
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
//        Mockito verify that the customer object has been passed
        verify(customerDAO).updateCustomer(argumentCaptor.capture());
        // grabbing the value from argumentCapture and assigning it to capturedCustomer
        Customer capturedCustomer = argumentCaptor.getValue();
        // this assertion is checking that the name field had been updated and matches
        assertThat(capturedCustomer.getName()).isEqualTo(customerUpdateRequest.name());
        // these assertions check that the email + age hasn't been updated and matches the original customer's data
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomerEmailOnly() {
        // GIVEN
        // create an id
        int id = 10;
        //create a new customer
        Customer customer = new Customer(
                "felix", "felix@aol.com", "password", 22,
                Gender.MALE);
        // use mockito to select the customer using the CustomerDAO instance
        // return optional object containing the customer object just created
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //create an update request with only the email -> null for name + age indicates only the email should be updated
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                null, "Felix123@aol.com", null
        );
        // WHEN
        // call the updateCustomer method with the id and customerUpdateRequest arguments
        customerServiceTest.updateCustomer(id, customerUpdateRequest);

        // THEN
        // create an ArgumentCapture object to capture the arg passed to the updateCustomer() method.
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        // Mockito verify that the customer object has been passed
        verify(customerDAO).updateCustomer(argumentCaptor.capture());
        // grabbing the value from argumentCapture and assigning it to capturedCustomer
        Customer capturedCustomer = argumentCaptor.getValue();
        // these assertions check that the name + age hasn't been updated and matches the original customer's data
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        // this assertion is checking that the email field had been updated and matches
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerUpdateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomerAgeOnly() {
        // Given
        // create an id
        int id = 10;
        // create a new customer to update
        Customer customer = new Customer(
                "felix", "felix@aol.com", "password", 22,
                Gender.MALE);
        // use mockito to select the customer using the CustomerDAO instance
        // return optional object containing the customer object just created
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //create an update request with only the age -> null for name + email indicates only the age should be updated
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                null, null, 45
        );
        // WHEN
        // call the updateCustomer method with the id and customerUpdateRequest arguments
        customerServiceTest.updateCustomer(id, customerUpdateRequest);

        // THEN
        // create an ArgumentCapture object to capture the arg passed to the updateCustomer() method.
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        // Mockito verify that the customer object has been passed
        verify(customerDAO).updateCustomer(argumentCaptor.capture());
        // grabbing the value from argumentCapture and assigning it to capturedCustomer
        Customer capturedCustomer = argumentCaptor.getValue();
        // these assertions check that the name + age hasn't been updated and matches the original customer's data
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        // this assertion is checking that the age field had been updated and matches
        assertThat(capturedCustomer.getAge()).isEqualTo(customerUpdateRequest.age());
    }


    @Test
    void WillThrowIfEmailExist() {
        // Given
        // create an id
        int id = 10;
        // create a new customer
        Customer customer = new Customer(
                "felix", "felix@aol.com", "password", 22,
                Gender.MALE);
        // Mockito grabbing the customer by id -> using Optional because it is of optional type
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // create the same email to be passed in to check if it exists
        String newEmail = "felix123@aol.com";
        // pass in the newEmail in the updateRequest
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                "Andy", newEmail, 25
        );
        // pass in the customerDAO instance and the existsPersonWithEmail() method -> return true -> checking that
        // the email already exists
        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(true);
        // When
        // assertion grabbing the updateCustomer() method -> passing in the id and new customer we updated with the
        // newEmail
        assertThatThrownBy(() -> customerServiceTest.updateCustomer(id, customerUpdateRequest))
                // make an instance of the DuplicateResourceException class
                .isInstanceOf(DuplicateResourceException.class)
                // input the message that s contained in the updateCustomer() method
                .hasMessage(
                        "Email already exist"  // <- has to be the same message or will get an error
                );

        // Then
        // Mockito to verify that the updateCustomer() method is never called -> method fails due to email existing
        verify(customerDAO, never()).updateCustomer(any());
    }


    @Test
    void WillThrowIfNoChangesExist() {
        // Given
        // create a new id
        int id = 10;
        // create a new customer
        Customer customer = new Customer(
                "felix", "felix@aol.com", "password", 22,
                Gender.MALE);
        // Mockito grabbing the customer by id -> using Optional because it is of optional type
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // create a new updateCustomerRequest but pass in the customer pre-existing name, age, and email that we made
        // above
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge()
        );
        // When
        // assertion grabbing the updateCustomer() method -> passing in the id and new customer we updated with the
        // same pre-existing values
        assertThatThrownBy(() -> customerServiceTest.updateCustomer(id, customerUpdateRequest))
                // make an instance of the RequestValidationException class
                .isInstanceOf(RequestValidationException.class)
                // input the message that s contained in the updateCustomer() method
                .hasMessage(
                        "No data was changed"  // <- must be the same message
                );

        // Then
        // Mockito to verify that the updateCustomer() method is never called -> method fails due to no changes
        verify(customerDAO, never()).updateCustomer(any());
    }


}