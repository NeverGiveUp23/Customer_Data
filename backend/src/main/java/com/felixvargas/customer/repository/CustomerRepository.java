package com.felixvargas.customer.repository;

import com.felixvargas.customer.model.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

// for testing, we only need to test this jpa repo when we add custom query's that are complex, must test
    // the ones below are provided by JPA -> testing isn't necessary


    boolean existsCustomerByEmail(String email);

    Optional<Customer> findCustomerByEmail(String email);

    boolean existsCustomerById(Integer id);

    @Modifying(clearAutomatically = true) // this ensures that the entity manager is cleared before the update is executed
    @Transactional // this ensures that the update is executed in a transactional context
    @Query("UPDATE Customer c SET c.profileImageId = ?1 WHERE c.id = ?2")
    void updateCustomerProfileImageId(String profileImageId, Integer customerId);


}
