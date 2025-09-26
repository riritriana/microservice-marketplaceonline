package com.cart_service.dto;

import lombok.Data;

@Data
public class AddItemRequestDTO {
    private Long productId;
    private int quantity;
}
