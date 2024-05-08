package com.bookshop.service;

import com.bookshop.dto.order.OrderRequestDto;
import com.bookshop.dto.order.OrderResponseDto;
import com.bookshop.dto.orderitem.OrderItemResponseDto;
import com.bookshop.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(User user,
                                 OrderRequestDto orderRequestDto);

    List<OrderResponseDto> findAllOrders(Long userId, Pageable pageable);

    List<OrderItemResponseDto> findAllByOrderId(Long orderId, Pageable pageable, Long userId);

    OrderItemResponseDto findOrderItemByIdInOrder(Long orderId, Long id, Long userId);

    OrderResponseDto updateOrderStatus(Long orderId, String newStatus);
}
