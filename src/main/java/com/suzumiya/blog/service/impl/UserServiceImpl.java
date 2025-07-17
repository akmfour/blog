package com.suzumiya.blog.service.impl;

import com.suzumiya.blog.common.ResultCode;
import com.suzumiya.blog.config.security.JwtService;
import com.suzumiya.blog.dto.AuthenticationResponseDto;
import com.suzumiya.blog.dto.LoginDto;
import com.suzumiya.blog.dto.RegistDto;
import com.suzumiya.blog.dto.UserDto;
import com.suzumiya.blog.entity.Role;
import com.suzumiya.blog.entity.RoleName;
import com.suzumiya.blog.entity.UserEntity;
import com.suzumiya.blog.exception.BusinessException;
import com.suzumiya.blog.mapper.UserMapper;
import com.suzumiya.blog.repository.RoleRepository;
import com.suzumiya.blog.repository.UserRepository;
import com.suzumiya.blog.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            @Lazy AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public UserDto registerNewUser(RegistDto registDto) throws BusinessException {
        if (userRepository.existsByEmail(registDto.getEmail())) {
            throw new BusinessException(ResultCode.USER_EMAIL_ALREADY_EXISTS, "Error: Email " + registDto.getEmail() + " has been used");
        }
        UserEntity newUser = new UserEntity();
        newUser.setUsername(registDto.getName());
        newUser.setEmail(registDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registDto.getPassword()));

        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Default role 'ROLE_USER' not found in database."));
        newUser.setRoles(Collections.singleton(userRole));

        UserEntity savedUser = userRepository.save(newUser);
        return UserMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public AuthenticationResponseDto login(LoginDto loginDto) {
        // 1. 使用 AuthenticationManager 進行用戶認證
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        // 2. 認證成功後，從資料庫查找用戶（因為需要用戶的完整資訊來生成 token）
        var user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new BusinessException(ResultCode.USER_ACCOUNT_NOT_FOUND, "User not found"));

        // 3. 生成 Access Token 和 Refresh Token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // 4. 將新的 Refresh Token 儲存到資料庫
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // 5. 返回兩種 token
        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout() {
        // 從安全上下文中獲取當前用戶的 email
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        // 根據 email 查找用戶並清除其 refresh token
        userRepository.findByEmail(username).ifPresent(user -> {
            user.setRefreshToken(null);
            userRepository.save(user);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 這個方法保持不變，它專門為 Spring Security 的認證流程服務
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}