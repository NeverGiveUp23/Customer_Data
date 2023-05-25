package com.felixvargas.customer.service;

import com.felixvargas.customer.model.Customer;
import com.felixvargas.customer.enums.Gender;
import com.felixvargas.customer.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;

    private AutoCloseable autoCloseable;

    @Mock
    private CustomerRepository customerRepositoryMock;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this); // <- close after each test
        underTest = new CustomerJPADataAccessService(customerRepositoryMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomer() {
        Page<Customer> page = mock(Page.class);
        List<Customer> customers = List.of(new Customer());
        when(page.getContent()).thenReturn(customers);
        when(customerRepositoryMock.findAll(any(Pageable.class))).thenReturn(page);
        // When
        List<Customer> expected = underTest.selectAllCustomer();

        // Then
        assertThat(expected).isEqualTo(customers);
        ArgumentCaptor<Pageable> pageArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(customerRepositoryMock).findAll(pageArgumentCaptor.capture());
        assertThat(pageArgumentCaptor.getValue()).isEqualTo(Pageable.ofSize(30));
    }

    @Test
    void selectCustomerById() {
        // Given
        int id = 1;
        // When
        underTest.selectCustomerById(id);

        // Then
        Mockito.verify(customerRepositoryMock)
                .findById(id);
    }

    @Test
    void existsPersonWithId() {
        // Given
        int id = 1;

        // When
        underTest.existsPersonWithId(id);
        // Then
        Mockito.verify(customerRepositoryMock).existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(
                "mike", "mike@aol.com", "password", 77,
                Gender.MALE);
        // When

        underTest.updateCustomer(customer);

        // Then

        Mockito.verify(customerRepositoryMock).save(customer);
    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 1;
        // When
        underTest.deleteCustomerById(id);

        // Then
        Mockito.verify(customerRepositoryMock).deleteById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(
                "Felix", "felix@aol.com", "password", 22,
                Gender.MALE);
        // When
        underTest.insertCustomer(customer);

        // Then
        Mockito.verify(customerRepositoryMock).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = "felix@aol.com";
        // When
        underTest.existsPersonWithEmail(email);

        // Then
        Mockito.verify(customerRepositoryMock).existsCustomerByEmail(email);
    }


    @Test
    void canUpdateProfileImageId() {

        String profileIMageId = "test";
        Integer customerId = 1;

        //When
        underTest.updateCustomerProfileImageId(profileIMageId, customerId);

        //then
        verify(customerRepositoryMock).updateCustomerProfileImageId(profileIMageId, customerId);
    }
}