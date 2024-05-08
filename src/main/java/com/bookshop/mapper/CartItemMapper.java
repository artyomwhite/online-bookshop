package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.cartitem.CartItemResponseDto;
import com.bookshop.dto.cartitem.CreateCartItemDto;
import com.bookshop.dto.cartitem.UpdateCartItemDto;
import com.bookshop.model.CartItem;
import com.bookshop.service.BookService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class,
        uses = BookService.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toResponseDto(CartItem cartItem);

    @Mapping(source = "bookId", target = "book")
    CartItem toCartItem(CreateCartItemDto createCartItemDto);

    void updateCartItem(UpdateCartItemDto updateCartItemDto, @MappingTarget CartItem cartItem);
}
