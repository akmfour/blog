package com.suzumiya.blog.common;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "Success"),

    // 客户端错误段 (1000 - 1999)
    BAD_REQUEST(1000, "HTTP 400 Bad Request"),
    UNAUTHORIZED(1001, "HTTP 401 Unauthorized"),
    FORBIDDEN(1002, "HTTP 403 Forbidden"),
    NOT_FOUND(1003, "HTTP 404 Not Found"),
    METHOD_NOT_ALLOWED(1004, "HTTP 405 Method Not Allowed"),
    REQUEST_TIMEOUT(1005, "HTTP 408 Request Timeout"),
    CONFLICT(1006, "HTTP 409 Conflict"),
    UNSUPPORTED_MEDIA_TYPE(1007, "HTTP 415 Unsupported Media Type"),
    TOO_MANY_REQUESTS(1008, "HTTP 429 Too Many Requests"),
    VALIDATION_ERROR(1009, "Parameter validation failure"),

    // 服务端错误段 (2000 - 2999)
    INTERNAL_SERVER_ERROR(2000, "HTTP 500 Internal Server Error"),
    SERVICE_UNAVAILABLE(2001, "HTTP 503 Service Unavailable"),
    GATEWAY_TIMEOUT(2002, "HTTP 504 Gateway Timeout"),
    DATABASE_ERROR(2003, "Database error"),
    NETWORK_ERROR(2004, "Network error"),
    THIRD_PARTY_SERVICE_ERROR(2005, "Third-party service error"),

    // ================================== 用户模块状态码 (3000 - 3999) ==================================
    // 注册相关
    // USER_REGISTRATION_SUCCESS(3000, "用户注册成功"),
    USER_EMAIL_ALREADY_EXISTS(3001, "Email already exists"),

    USER_USERNAME_ALREADY_EXISTS(3002, "Username"),

    USER_EMAIL_NOT_VALID(3006, "Email is not valid"),

    USER_PASSWORD_TOO_SHORT(3003, "password too short"),
    USER_PASSWORD_TOO_WEAK(3004, "password too weak"),
    USER_REGISTRATION_FAILED(3005, "User registration failed"),

    // 登录相关
    // USER_LOGIN_SUCCESS(3100, "登录成功"),
    USER_ACCOUNT_NOT_FOUND(3101, "User account not found"),
    USER_INVALID_CREDENTIALS(3102, "User invalid credentials"),
    USER_ACCOUNT_LOCKED(3103, "User account locked"),
    USER_ACCOUNT_DISABLED(3104, "User account disabled"),
    USER_ACCOUNT_EXPIRED(3105, "User account expired"),
    USER_LOGIN_FAILED(3106, "User login failed"),
    USER_SESSION_EXPIRED(3107, "User session expired"),
    USER_TOKEN_INVALID(3108, "User token invalid"),
    USER_TOKEN_EXPIRED(3109, "User token expired(Token"),
    USER_REFRESH_TOKEN_INVALID(3110, "User refresh token invalid"),
    // USER_REFRESH_TOKEN_EXPIRED(3111, "User refresh token expired(Refresh Token"),
    USER_LOGOUT_SUCCESS(3112, "loignout success"),

    // 用户信息相关
    USER_PROFILE_NOT_FOUND(3200, "User profile not found"),
    USER_UPDATE_PROFILE_SUCCESS(3201, "User profile updated"),
    USER_UPDATE_PROFILE_FAILED(3202, "User profile update failed"),
    USER_CHANGE_PASSWORD_SUCCESS(3203, "Change password success"),
    USER_CHANGE_PASSWORD_FAILED(3204, "Change password failed"),
    USER_OLD_PASSWORD_MISMATCH(3205, "Old password mismatch"),

    // 权限相关 (如果你的用户模块包含复杂权限)
    // USER_PERMISSION_DENIED(3300, "用户权限不足（细粒度）"), // 可用于补充 FORBIDDEN

    ;

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}