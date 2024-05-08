package com.bookshop.service;

import com.bookshop.dto.book.BookDtoWithoutCategoryIds;
import com.bookshop.dto.category.CategoryDto;
import com.bookshop.dto.category.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId);

    CategoryDto save(CreateCategoryRequestDto requestDto);

    CategoryDto updateCategory(Long id, CreateCategoryRequestDto requestDto);

    void deleteCategoryById(Long id);
}
