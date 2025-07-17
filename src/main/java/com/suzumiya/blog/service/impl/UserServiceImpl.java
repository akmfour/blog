package com.suzumiya.blog.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suzumiya.blog.common.ResultCode;
import com.suzumiya.blog.dto.RegistDto;
import com.suzumiya.blog.entity.UserEntity;
import com.suzumiya.blog.exception.BusinessException;
import com.suzumiya.blog.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerNewUser(@NotNull RegistDto registDto) throws BusinessException {

        if (userRepository.existsByEmail(registDto.getEmail())) {
            throw new BusinessException(ResultCode.USER_EMAIL_ALREADY_EXISTS,"error: Email" + registDto.getEmail() + " had been used");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUserName(registDto.getName());
        newUser.setEmail(registDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registDto.getPassword()));

        return userRepository.save(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found"));

        Collection<? extends GrantedAuthority> authorities = Collections
                .singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(
                user.getEmail(),
                user.getPassword(),
                true, // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities // role
        );
    }
}