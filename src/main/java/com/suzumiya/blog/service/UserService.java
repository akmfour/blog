package com.suzumiya.blog.service;

import com.suzumiya.blog.dto.AuthenticationResponseDto;
import com.suzumiya.blog.dto.LoginDto;
import com.suzumiya.blog.dto.RegistDto;
import com.suzumiya.blog.dto.UserDto;
import com.suzumiya.blog.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserDto registerNewUser(RegistDto registDto) throws BusinessException;

    AuthenticationResponseDto login(LoginDto loginDto);

    void logout();
}
