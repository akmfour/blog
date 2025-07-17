package com.suzumiya.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistDto {

    @Size(min = 2, max = 20, message = "用户名长度必须在2到20个字符之间")
    @Pattern(
            regexp = "^[a-zA-Z\\p{script=Han}]+$",
            message = "用户名只能包含大小写英文字母和汉字，不能使用数字和标点符号"
    )
    private String name;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确，请输入有效的邮箱地址")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度必须在6到30位之间")
    private String password;
}
