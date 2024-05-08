package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.category.CategoryDto;
import com.bookshop.dto.category.CreateCategoryRequestDto;
import com.bookshop.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    Category toCategory(CreateCategoryRequestDto requestDto);

    CategoryDto toDto(Category category);

    void updateCategory(CreateCategoryRequestDto dto, @MappingTarget Category category);
}
