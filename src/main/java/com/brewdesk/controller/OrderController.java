package com.brewdesk.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brewdesk.dto.order.OrderItemRequest;
import com.brewdesk.dto.order.OrderRequest;
import com.brewdesk.dto.order.OrderResponse;
import com.brewdesk.dto.order.OrderStatusRequest;
import com.brewdesk.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid OrderRequest orderRequest) {
        OrderResponse response = orderService.create(orderRequest);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<OrderResponse> addItem(@PathVariable Long id,
            @RequestBody @Valid OrderItemRequest itemRequest) {
        OrderResponse response = orderService.addItem(id, itemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        List<OrderResponse> responses = orderService.findAll();
        return ResponseEntity.status(200).body(responses);
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
            @Valid @RequestBody OrderStatusRequest request) {
        OrderResponse response = orderService.updateStatus(id, request.status(), request.observation());
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<OrderResponse> removeItem(@PathVariable Long id, @PathVariable Long itemId) {
        OrderResponse response = orderService.decrementItem(id, itemId);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        OrderResponse response = orderService.findById(id);
        return ResponseEntity.ok(response);
    }
}
