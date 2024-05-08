package com.bookshop.controller;

import com.bookshop.dto.book.BookDtoWithoutCategoryIds;
import com.bookshop.dto.category.CategoryDto;
import com.bookshop.dto.category.CreateCategoryRequestDto;
import com.bookshop.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Tag(name = "Category management",
        description = "Browse and manage categories depending on a role")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Retrieve all categories",
            description = "Retrieve categories using pagination")
    @PreAuthorize("hasRole('USER')")
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Retrieve all books by category id",
                description = "Check if id is valid and retrieve all books from the category")
    @PreAuthorize("hasRole('USER')")
    public List<BookDtoWithoutCategoryIds> getAllByCategory(@PathVariable @Positive Long id) {
        return categoryService.findAllByCategoryId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save a category to DB",
            description = "Save category name and description to DB")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto saveCategory(@RequestBody CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category from DB",
            description = "Check if a category with such id exists and update this category")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto updateCategory(@PathVariable @Positive Long id,
                                      @RequestBody CreateCategoryRequestDto requestDto) {
        return categoryService.updateCategory(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category from DB",
            description = "Delete a category from DB")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }
}
