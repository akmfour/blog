package com.suzumiya.blog.controller;

import com.suzumiya.blog.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogController {

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<Object>> home(){

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}