package com.bookshop.service.impl;

import com.bookshop.dto.user.UserRegistrationRequestDto;
import com.bookshop.dto.user.UserResponseDto;
import com.bookshop.exception.RegistrationException;
import com.bookshop.mapper.UserMapper;
import com.bookshop.model.Role;
import com.bookshop.model.ShoppingCart;
import com.bookshop.model.User;
import com.bookshop.repository.RoleRepository;
import com.bookshop.repository.UserRepository;
import com.bookshop.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RegistrationException("Unable to complete registration. User already exists");
        }
        Role defaultRole = roleRepository.findRoleByName(Role.RoleName.USER);
        User user = userMapper.toUser(request);
        user.setRoles(Set.of(defaultRole));
        user.setPassword(passwordEncoder.encode(request.password()));
        user.addShoppingCart(new ShoppingCart());
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
