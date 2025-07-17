package com.suzumiya.blog.controller;

import com.suzumiya.blog.common.ApiResponse;
import com.suzumiya.blog.common.ResultCode;
import com.suzumiya.blog.dto.AuthenticationResponseDto;
import com.suzumiya.blog.dto.LoginDto;
import com.suzumiya.blog.dto.RegistDto;
import com.suzumiya.blog.dto.UserDto;
import com.suzumiya.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> registerUser(@Valid @RequestBody RegistDto registDto) {

        UserDto userDto = userService.registerNewUser(registDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponseDto>> authenticateUser(@Valid @RequestBody LoginDto loginDto) {

        AuthenticationResponseDto tokens = userService.login(loginDto);

        return ResponseEntity.ok(ApiResponse.success(tokens));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logoutUser() {

        userService.logout();

        return ResponseEntity.ok(ApiResponse.success("Logout successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDetails>> UserInfoInit(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail(ResultCode.USER_TOKEN_INVALID, null));
        }

        return ResponseEntity.ok(ApiResponse.success(userDetails));
    }
}
