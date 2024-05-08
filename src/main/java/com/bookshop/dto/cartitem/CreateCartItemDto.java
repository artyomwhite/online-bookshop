package com.bookshop.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemDto(@NotNull @Positive Long bookId,
                                @Positive Integer quantity) {
}
