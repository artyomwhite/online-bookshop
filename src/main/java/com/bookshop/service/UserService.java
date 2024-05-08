package com.bookshop.service;

import com.bookshop.dto.user.UserRegistrationRequestDto;
import com.bookshop.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request);
}
