package com.bookshop.dto.order;

import com.bookshop.dto.orderitem.OrderItemResponseDto;
import com.bookshop.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderResponseDto(Long userId,
                               Long orderId,
                               Order.Status status,
                               BigDecimal total,
                               LocalDateTime orderDate,
                               String shippingAddress,
                               Set<OrderItemResponseDto> orderItems) {
}
