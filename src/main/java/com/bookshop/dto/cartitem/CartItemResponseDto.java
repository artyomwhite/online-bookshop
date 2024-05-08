package com.bookshop.dto.cartitem;

public record CartItemResponseDto(Long id,
                                  Long bookId,
                                  String bookTitle,
                                  int quantity) {
}
