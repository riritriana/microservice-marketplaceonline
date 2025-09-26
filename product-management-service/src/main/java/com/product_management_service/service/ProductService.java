package com.product_management_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.product_management_service.dto.ProductRequestDTO;
import com.product_management_service.model.Product;
import com.product_management_service.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Mengambil semua produk dari database.
     * @return Daftar semua produk.
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Mencari satu produk berdasarkan ID-nya.
     * @param id ID produk yang dicari.
     * @return Produk yang ditemukan.
     * @throws RuntimeException jika produk tidak ditemukan.
     */
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * Menyimpan produk baru ke database.
     * @param product Objek produk yang akan disimpan.
     * @return Produk yang telah disimpan.
     */
 /**
     * Menyimpan produk baru dari DTO.
     * @param requestDTO Objek DTO dengan data produk baru.
     * @return Entitas Produk yang telah disimpan.
     */
    public Product save(ProductRequestDTO requestDTO) {
        Product product = new Product();
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());
        return productRepository.save(product);
    }
/**
     * Memperbarui data produk dari DTO.
     * @param id ID produk yang akan diperbarui.
     * @param requestDTO Objek DTO dengan data baru.
     * @return Entitas Produk yang telah diperbarui.
     */
    public Product update(Long id, ProductRequestDTO requestDTO) {
        Product product = findById(id);
        
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());
        
        return productRepository.save(product);
    }
    /**
     * Menghapus produk berdasarkan ID.
     * @param id ID produk yang akan dihapus.
     */
    public void deleteById(Long id) {
        // Cek dulu apakah produknya ada sebelum dihapus
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}