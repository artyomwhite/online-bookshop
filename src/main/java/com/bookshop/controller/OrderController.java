package com.bookshop.controller;

import com.bookshop.dto.order.OrderRequestDto;
import com.bookshop.dto.order.OrderResponseDto;
import com.bookshop.dto.order.OrderStatusUpdateDto;
import com.bookshop.dto.orderitem.OrderItemResponseDto;
import com.bookshop.model.User;
import com.bookshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Management",
        description = "Endpoints to create, update and get orders and order items")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create order",
                description = """
                        User can create order.
                        After all cart items are deleted.
                        Params: shipping address""")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto createOrder(Authentication authentication,
                                        @RequestBody @Valid OrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(user, requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all orders",
                description = "User can get order history")
    @PreAuthorize("hasRole('USER')")
    public List<OrderResponseDto> getAllOrders(Authentication authentication,
                                               Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrders(user.getId(), pageable);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order items from order",
            description = "User can get all order items from a specific order")
    @PreAuthorize("hasRole('USER')")
    public List<OrderItemResponseDto> getOrderItemsOfOrder(@PathVariable @Positive Long orderId,
                                                           @PageableDefault(size = 7)
                                                           Pageable pageable,
                                                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllByOrderId(orderId, pageable, user.getId());
    }

    @GetMapping("{orderId}/items/{id}")
    @Operation(summary = "Get one order item from order",
            description = "User can get a certain order item from a certain order")
    @PreAuthorize("hasRole('USER')")
    public OrderItemResponseDto getOrderItemFromOrder(@PathVariable @Positive Long orderId,
                                                      @PathVariable @Positive Long id,
                                                      Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findOrderItemByIdInOrder(orderId, id, user.getId());
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "Update order status",
            description = "Admin can change status of a certain order")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponseDto updateStatus(@PathVariable @Positive Long orderId,
                                         @RequestBody @Valid OrderStatusUpdateDto updateDto) {
        return orderService.updateOrderStatus(orderId, updateDto.newStatus());
    }
}
