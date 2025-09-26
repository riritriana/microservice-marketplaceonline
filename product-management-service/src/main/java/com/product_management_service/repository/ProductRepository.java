package com.product_management_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.product_management_service.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
