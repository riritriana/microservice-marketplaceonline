package com.cart_service.dto;

import lombok.Data;

@Data
public class ChekoutResponseDTO {
    private String orderId;
    private String status;
    private String message;
    private double totalAmount;
}
