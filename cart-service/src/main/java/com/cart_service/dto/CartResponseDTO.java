package com.cart_service.dto;

import java.util.List;
import lombok.Data;

@Data
public class CartResponseDTO {
    private String userEmail;
    private List<CartItemResponseDTO> items;
    private double totalPrice;
}
