package com.cart_service.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cart_service.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserEmail(String userEmail);
}