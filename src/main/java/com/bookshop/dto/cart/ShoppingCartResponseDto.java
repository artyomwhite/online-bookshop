package com.bookshop.dto.cart;

import com.bookshop.dto.cartitem.CartItemResponseDto;
import java.util.Set;

public record ShoppingCartResponseDto(Long id,
                                      Long userId,
                                      Set<CartItemResponseDto> cartItems) {
}
