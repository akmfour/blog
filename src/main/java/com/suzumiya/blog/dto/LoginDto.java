package com.suzumiya.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class LoginDto {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确，请输入有效的邮箱地址")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度必须在6到30位之间")
    private String password;
}