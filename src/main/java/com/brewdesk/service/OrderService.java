package com.brewdesk.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.brewdesk.domain.Order;
import com.brewdesk.domain.OrderItem;
import com.brewdesk.domain.Product;
import com.brewdesk.dto.category.CategoryResponse;
import com.brewdesk.dto.order.OrderItemRequest;
import com.brewdesk.dto.order.OrderItemResponse;
import com.brewdesk.dto.order.OrderRequest;
import com.brewdesk.dto.order.OrderResponse;
import com.brewdesk.dto.product.ProductResponse;
import com.brewdesk.enums.OrderStatus;
import com.brewdesk.repository.OrderRepository;
import com.brewdesk.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

        private final ProductRepository productRepository;
        private final OrderRepository orderRepository;

        public OrderResponse create(OrderRequest orderRequest) {
                Order order = new Order();
                order.setCustomerName(orderRequest.customerName());
                order.setStatus(OrderStatus.OPEN);
                order.setCreatedAt(LocalDateTime.now());
                order.setOrderItems(new ArrayList<>());
                order.setTotal(BigDecimal.ZERO);

                Order savedOrder = orderRepository.save(order);

                return toResponse(savedOrder);
        }

        public OrderResponse addItem(Long orderId, OrderItemRequest itemRequest) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

                Product product = productRepository.findById(itemRequest.productId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Product not found with id: " + itemRequest.productId()));

                // Verifica se o produto já existe no pedido
                OrderItem existingItem = order.getOrderItems().stream()
                                .filter(item -> item.getProduct().getId().equals(itemRequest.productId()))
                                .findFirst()
                                .orElse(null);

                if (existingItem != null) {
                        // Incrementa a quantidade do item existente
                        existingItem.setQuantity(existingItem.getQuantity() + itemRequest.quantity());
                        existingItem.setSubtotal(
                                        product.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
                } else {
                        // Cria um novo item
                        OrderItem item = new OrderItem();
                        item.setOrder(order);
                        item.setProduct(product);
                        item.setQuantity(itemRequest.quantity());
                        item.setUnitPrice(product.getPrice());
                        item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
                        item.setObservation(itemRequest.observation());
                        order.getOrderItems().add(item);
                }

                // Recalcula o total
                BigDecimal newTotal = order.getOrderItems().stream()
                                .map(OrderItem::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                order.setTotal(newTotal);

                Order savedOrder = orderRepository.save(order);

                return toResponse(savedOrder);
        }

        public OrderResponse decrementItem(Long orderId, Long itemId) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

                OrderItem item = order.getOrderItems().stream()
                                .filter(i -> i.getId().equals(itemId))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));

                if (item.getQuantity() <= 1) {
                        // Remove o item se a quantidade chegar a 0
                        order.getOrderItems().remove(item);
                } else {
                        // Decrementa a quantidade
                        item.setQuantity(item.getQuantity() - 1);
                        item.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                }

                // Recalcula o total
                BigDecimal newTotal = order.getOrderItems().stream()
                                .map(OrderItem::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                order.setTotal(newTotal);

                Order savedOrder = orderRepository.save(order);

                return toResponse(savedOrder);
        }

        public List<OrderResponse> findAll() {
                return orderRepository.findAll().stream()
                                .map(this::toResponse)
                                .toList();
        }

        public OrderResponse deleteOrder(Long id) {
                Order order = orderRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

                orderRepository.delete(order);

                return toResponse(order);
        }

        public OrderResponse updateStatus(Long orderId, OrderStatus status, String observation) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
                order.setStatus(status);
                order.setObservation(observation);
                return toResponse(orderRepository.save(order));
        }

        public OrderResponse findById(Long id) {
                Order order = orderRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
                return toResponse(order);
        }

        private OrderResponse toResponse(Order order) {
                return new OrderResponse(
                                order.getId(),
                                order.getCustomerName(),
                                order.getStatus(),
                                order.getCreatedAt(),
                                order.getTotal(),
                                order.getOrderItems().stream()
                                                .map(item -> new OrderItemResponse(
                                                                item.getId(),
                                                                new ProductResponse(
                                                                                item.getProduct().getId(),
                                                                                item.getProduct().getName(),
                                                                                item.getProduct().getDescription(),
                                                                                item.getProduct().getPrice(),
                                                                                new CategoryResponse(
                                                                                                item.getProduct()
                                                                                                                .getCategory()
                                                                                                                .getId(),
                                                                                                item.getProduct()
                                                                                                                .getCategory()
                                                                                                                .getName())),
                                                                item.getQuantity(),
                                                                item.getUnitPrice(),
                                                                item.getSubtotal(),
                                                                item.getObservation()))
                                                .toList(),
                                order.getObservation());
        }

        public OrderResponse updateObservation(Long orderId, String observation) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
                order.setObservation(observation);
                return toResponse(orderRepository.save(order));
        }
}