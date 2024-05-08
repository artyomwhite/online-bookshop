package com.bookshop.controller;

import com.bookshop.dto.cart.ShoppingCartResponseDto;
import com.bookshop.dto.cartitem.CartItemResponseDto;
import com.bookshop.dto.cartitem.CreateCartItemDto;
import com.bookshop.dto.cartitem.UpdateCartItemDto;
import com.bookshop.model.User;
import com.bookshop.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart Management",
        description = "Add, update and delete cart items in a shopping cart.")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @Operation(summary = "Add cart item to a shopping cart",
            description = "A user can add a cart item to their shopping cart.")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto addBookToCart(
            @RequestBody @Valid CreateCartItemDto cartItemDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.save(cartItemDto, user);
    }

    @GetMapping
    @Operation(summary = "Find all cart items in a shopping cart",
            description = "User can find all cart items in their shopping cart.")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto findShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.findByUserId(user.getId());
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete a cart item from shopping cart",
            description = """
                    User can delete a cart item from their shopping cart.
                    "If a book is deleted from DB, 
                    cart items with such book is deleted from all shopping carts as well.
                    """)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER')")
    public void deleteCartItemFromShoppingCart(
            @PathVariable @Positive Long cartItemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteCartItemFromShoppingCart(user.getId(), cartItemId);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update a cart item",
            description = "User can change book quantity in cart item in their shopping cart.")
    @PreAuthorize("hasRole('USER')")
    public CartItemResponseDto updateCartItem(
            @PathVariable @Positive Long cartItemId,
            Authentication authentication,
            @RequestBody @Valid UpdateCartItemDto cartItemDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItem(user.getId(), cartItemId, cartItemDto);
    }
}
