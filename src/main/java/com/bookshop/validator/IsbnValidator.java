package com.bookshop.validator;

import com.bookshop.validator.annotation.Isbn;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    @Override
    public boolean isValid(String isbnField, ConstraintValidatorContext context) {
        return isbnField != null && isbnField.matches(
                "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$");
    }
}
