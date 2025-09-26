package com.cart_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.cart_service.dto.AddItemRequestDTO;
import com.cart_service.dto.CartItemResponseDTO;
import com.cart_service.dto.CartResponseDTO;
import com.cart_service.dto.ProductDTO;
import com.cart_service.model.Cart;
import com.cart_service.model.CartItem;
import com.cart_service.repository.CartRepository;

import reactor.core.publisher.Mono;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Transactional
    public void addItemToCart(String userEmail, AddItemRequestDTO request) {
        // Panggil Product Service untuk validasi produk dan stok
        ProductDTO product = getProductById(request.getProductId())
            .blockOptional()
            .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Cari atau buat keranjang baru untuk user
        Cart cart = cartRepository.findByUserEmail(userEmail).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserEmail(userEmail);
            return newCart;
        });

        // Tambah atau update item di keranjang
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(request.getProductId());
            newItem.setQuantity(request.getQuantity());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }
        cartRepository.save(cart);
    }
    
    public CartResponseDTO getCart(String userEmail) {
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElse(new Cart()); // Kembalikan keranjang kosong jika tidak ada
        
        cart.setUserEmail(userEmail);

        CartResponseDTO responseDTO = new CartResponseDTO();
        responseDTO.setUserEmail(cart.getUserEmail());

        // Untuk setiap item di cart, panggil product service untuk dapat info terbaru
        responseDTO.setItems(cart.getItems().stream().map(item -> {
            ProductDTO product = getProductById(item.getProductId()).block();
            CartItemResponseDTO itemDTO = new CartItemResponseDTO();
            if (product != null && product.getId() != null) {
                itemDTO.setProductId(product.getId());
                itemDTO.setProductName(product.getName());
                itemDTO.setPrice(product.getPrice());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setSubtotal(product.getPrice() * item.getQuantity());
            }
            return itemDTO;
        }).collect(Collectors.toList()));
        
        // Hitung total harga
        double totalPrice = responseDTO.getItems().stream()
            .mapToDouble(CartItemResponseDTO::getSubtotal)
            .sum();
        responseDTO.setTotalPrice(totalPrice);
        
        return responseDTO;
    }

    @Transactional
    public void clearCart(String userEmail) {
        cartRepository.findByUserEmail(userEmail).ifPresent(cartRepository::delete);
    }
    
    // Metode helper untuk memanggil Product Service
    private Mono<ProductDTO> getProductById(Long productId) {
        String url = productServiceUrl + "/products/" + productId;
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .onErrorReturn(new ProductDTO()); // Kembalikan DTO kosong jika produk tidak ditemukan atau error
    }
}