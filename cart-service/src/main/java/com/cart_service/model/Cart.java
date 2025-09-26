package com.cart_service.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String userEmail;
    @OneToMany(mappedBy= "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();

}
