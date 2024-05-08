package com.bookshop.service.impl;

import com.bookshop.dto.cart.ShoppingCartResponseDto;
import com.bookshop.dto.cartitem.CartItemResponseDto;
import com.bookshop.dto.cartitem.CreateCartItemDto;
import com.bookshop.dto.cartitem.UpdateCartItemDto;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.CartItemMapper;
import com.bookshop.mapper.ShoppingCartMapper;
import com.bookshop.model.CartItem;
import com.bookshop.model.ShoppingCart;
import com.bookshop.model.User;
import com.bookshop.repository.CartItemRepository;
import com.bookshop.repository.ShoppingCartRepository;
import com.bookshop.repository.UserRepository;
import com.bookshop.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Transactional
    @Override
    public ShoppingCartResponseDto save(CreateCartItemDto createCartItemDto, User user) {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("User with email %s isn't found"
                        .formatted(user.getEmail()))
        );
        ShoppingCart shoppingCart = existingUser.getShoppingCart();
        CartItem cartItem = cartItemMapper.toCartItem(createCartItemDto);
        shoppingCart.addCartItem(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Transactional(readOnly = true)
    @Override
    public ShoppingCartResponseDto findByUserId(Long id) {
        ShoppingCart shoppingCart = getCartByUserId(id);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public void deleteCartItemFromShoppingCart(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = getCartByUserId(userId);
        CartItem cartItem = getCartItemById(cartItemId);
        shoppingCart.removeCartItem(cartItem);
    }

    @Transactional
    @Override
    public CartItemResponseDto updateCartItem(Long userId,
                                              Long cartItemId,
                                              UpdateCartItemDto cartItemDto) {
        ShoppingCart shoppingCart = getCartByUserId(userId);
        CartItem cartItem = getCartItemById(cartItemId);
        if (cartItem.getShoppingCart().getId().equals(shoppingCart.getId())) {
            cartItemMapper.updateCartItem(cartItemDto, cartItem);
            return cartItemMapper.toResponseDto(cartItem);
        } else {
            throw new EntityNotFoundException(
                    "User with id %d doesn't have cart item with id %d"
                            .formatted(userId, cartItemId));
        }
    }

    private ShoppingCart getCartByUserId(Long id) {
        return shoppingCartRepository.findShoppingCartByUserId(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("There is no user with the id: " + id));
    }

    private CartItem getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException(
                        "There is no cart item with the id: " + cartItemId)
        );
    }
}
