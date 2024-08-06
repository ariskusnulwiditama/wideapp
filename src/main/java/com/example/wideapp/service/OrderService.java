package com.example.wideapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wideapp.entity.Order;
import com.example.wideapp.entity.OrderItem;
import com.example.wideapp.entity.Product;
import com.example.wideapp.repository.OrderRepository;
import com.example.wideapp.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order saveOrder(Order order) {
        // Check if all product IDs in the order exist
        for (OrderItem item : order.getItems()) {
            Optional<Product> product = productRepository.findById(item.getProductId());
            if (!product.isPresent()) {
                throw new IllegalArgumentException("Product with ID " + item.getProductId() + " does not exist");
            }
        }

        // Calculate total if all product IDs are valid
        double total = calculateTotal(order);
        order.setTotal(total);
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Long id, Order order) {
        return orderRepository.findById(id).map(ord -> {
            ord.setCustomerName(order.getCustomerName());
            ord.setStatus(order.getStatus());
            ord.setItems(order.getItems());
            ord.setTotal(calculateTotal(order));
            return orderRepository.save(ord);
        }).orElseThrow(() -> new IllegalArgumentException("Order with ID " + id + " does not exist"));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private double calculateTotal(Order order) {
        return order.getItems().stream()
            .mapToDouble(item -> {
                Optional<Product> product = productRepository.findById(item.getProductId());
                return product.map(value -> value.getPrice() * item.getQuantity()).orElse(0.0);
            })
            .sum();
    }

}