package com.product_management_service.dto;
import lombok.Data;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;
}
