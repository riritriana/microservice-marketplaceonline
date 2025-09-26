package com.cart_service.dto;

import lombok.Data;

@Data
public class CartItemResponseDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
    private double subtotal;
}
