package com.zeel.inventory_api.service;


import com.zeel.inventory_api.dto.OrderRequest;
import com.zeel.inventory_api.entity.*;
import com.zeel.inventory_api.exception.ResourceNotFoundException;
import com.zeel.inventory_api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public Page<Order> getAllOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Order> getOrdersByStatus(Order.OrderStatus status, int page, int size) {
        return orderRepository.findByStatus(status, PageRequest.of(page, size));
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    public Order createOrder(OrderRequest request) {
        Product product = productService.getProductById(request.getProductId());

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        // deduct stock
        product.setStockQuantity(product.getStockQuantity() - request.getQuantity());

        Order order = new Order();
        order.setProduct(product);
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(product.getPrice() * request.getQuantity());

        return orderRepository.save(order);
    }

    public Order updateStatus(Long id, Order.OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}