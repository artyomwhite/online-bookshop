package com.bookshop.service;

import com.bookshop.dto.cart.ShoppingCartResponseDto;
import com.bookshop.dto.cartitem.CartItemResponseDto;
import com.bookshop.dto.cartitem.CreateCartItemDto;
import com.bookshop.dto.cartitem.UpdateCartItemDto;
import com.bookshop.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto save(CreateCartItemDto createCartItemDto, User user);

    ShoppingCartResponseDto findByUserId(Long id);

    void deleteCartItemFromShoppingCart(Long userId, Long cartItemId);

    CartItemResponseDto updateCartItem(Long userId, Long cartItemId, UpdateCartItemDto cartItemDto);
}
