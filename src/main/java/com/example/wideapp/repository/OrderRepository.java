package com.example.wideapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wideapp.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}