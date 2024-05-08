package com.bookshop.validator.annotation;

import com.bookshop.validator.IsbnValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IsbnValidator.class})
public @interface Isbn {
    String message() default "ISBN should contain 10 or 13 numbers";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
