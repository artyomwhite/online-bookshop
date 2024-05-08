package com.bookshop.dto.orderitem;

public record OrderItemResponseDto(Long id,
                                   Long bookId,
                                   Integer quantity) {
}
