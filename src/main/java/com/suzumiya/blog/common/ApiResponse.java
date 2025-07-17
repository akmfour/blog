package com.suzumiya.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private int code;
    private String message;
    private T data;

    //SUCCESS
    public static <T> ApiResponse<T> success(T data) {
        // 直接使用 ResultCode.SUCCESS 来构造
        ResultCode rc = ResultCode.SUCCESS;
        return new ApiResponse<>(true, rc.getCode(), rc.getMessage(), data);
    }

    //Fail
    public static <T> ApiResponse<T> fail(ResultCode resultCode, T data) {
        return new ApiResponse<>(false, resultCode.getCode(), resultCode.getMessage(), data);
    }
}
