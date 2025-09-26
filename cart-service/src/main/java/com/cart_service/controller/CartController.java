package com.cart_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.cart_service.dto.AddItemRequestDTO;
import com.cart_service.dto.CartResponseDTO;
import com.cart_service.dto.ChekoutResponseDTO;
import com.cart_service.service.CartService;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getMyCart(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(cartService.getCart(userEmail));
    }

    @PostMapping("/items")
    public ResponseEntity<String> addItemToCart(@RequestBody AddItemRequestDTO request, Authentication authentication) {
        String userEmail = authentication.getName();
        cartService.addItemToCart(userEmail, request);
        return ResponseEntity.ok("Item added to cart successfully.");
    }
    
    @PostMapping("/checkout")
    public ResponseEntity<ChekoutResponseDTO> checkout(Authentication authentication) {
        String userEmail = authentication.getName();
        CartResponseDTO cart = cartService.getCart(userEmail);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot proceed to checkout.");
        }
        
        String orderId = "ORDER-" + UUID.randomUUID().toString();
        
        cartService.clearCart(userEmail);
        
        ChekoutResponseDTO response = new ChekoutResponseDTO();
        response.setOrderId(orderId);
        response.setStatus("SUCCESS");
        response.setMessage("Order placed successfully. Thank you for your purchase!");
        response.setTotalAmount(cart.getTotalPrice());
        
        return ResponseEntity.ok(response);
    }
}