package com.bookshop.dto.book;

import com.bookshop.validator.annotation.Isbn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

public record CreateBookRequestDto(@NotBlank String title,
                                   @NotBlank String author,
                                   @Isbn
                                   String isbn,
                                   @NotNull @PositiveOrZero BigDecimal price,
                                   @Size(max = 255) String description,
                                   @Size(max = 255) String coverImage,
                                   Set<Long> categoriesIds) {

}
