package com.suzumiya.blog.controller;


import com.suzumiya.blog.common.ApiResponse;
import com.suzumiya.blog.common.ResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class BlogController {

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<Object>> home(){

        return ResponseEntity.ok(ApiResponse.success(null, ResultCode.SUCCESS));
    }


}
