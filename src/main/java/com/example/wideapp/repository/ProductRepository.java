package com.example.wideapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wideapp.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}