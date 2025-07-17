package com.suzumiya.blog.exception;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.suzumiya.blog.common.ApiResponse;
import com.suzumiya.blog.common.ResultCode;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // slf4j日志记录器
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 业务异常
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
        logger.warn("业务异常: Code={}, Message={}, Detail={}", ex.getResultCode().getCode(), ex.getResultCode().getMessage(), ex.getDetailMessage(), ex);

        ApiResponse<Object> body = ApiResponse.fail(ex.getResultCode(), ex.getDetailMessage());
        HttpStatus httpStatus = determineHttpStatusFromResultCode(ex.getResultCode());

        return new ResponseEntity<>(body, httpStatus);
    }

    // 参数校验异常
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(BindException ex) { // BindException 是 MethodArgumentNotValidException 的父类
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        logger.warn("参数校验失败: {}", errorMessage, ex);
        return new ResponseEntity<>(ApiResponse.fail(ResultCode.VALIDATION_ERROR, errorMessage), HttpStatus.BAD_REQUEST);
    }

    // 参数缺失异常
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String message = "请求参数 '" + ex.getParameterName() + "' 不能为空";
        logger.warn(message, ex);
        return new ResponseEntity<>(ApiResponse.fail(ResultCode.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

    // Spring Security 用户认证异常
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        logger.warn("认证失败: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(
                ApiResponse.fail(ResultCode.USER_INVALID_CREDENTIALS, "用户名或密码错误"),
                HttpStatus.UNAUTHORIZED
        );
    }

    // Spring Security 权限异常
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn("权限不足: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ApiResponse.fail(ResultCode.FORBIDDEN,null), HttpStatus.FORBIDDEN);
    }

    // 其他异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllUncaughtException(Exception ex) {
        logger.error("发生未捕获的服务器内部错误!", ex);

        return new ResponseEntity<>(ApiResponse.fail(ResultCode.INTERNAL_SERVER_ERROR,null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus determineHttpStatusFromResultCode(ResultCode resultCode) {
        if (resultCode == null) return HttpStatus.INTERNAL_SERVER_ERROR;

        switch (resultCode) {
            case SUCCESS:
                return HttpStatus.OK;
            case UNAUTHORIZED:
            case USER_INVALID_CREDENTIALS:
            case USER_TOKEN_INVALID:
            case USER_TOKEN_EXPIRED:
                return HttpStatus.UNAUTHORIZED;
            case FORBIDDEN:
                return HttpStatus.FORBIDDEN;
            case NOT_FOUND:
            case USER_ACCOUNT_NOT_FOUND:
            case USER_PROFILE_NOT_FOUND:
                return HttpStatus.NOT_FOUND;
            case CONFLICT:
            case USER_EMAIL_ALREADY_EXISTS:
                return HttpStatus.CONFLICT;
            case BAD_REQUEST:
            case VALIDATION_ERROR:
            case USER_PASSWORD_TOO_SHORT:
                return HttpStatus.BAD_REQUEST;
            default:
                if (resultCode.getCode() >= 2000 && resultCode.getCode() < 3000) {
                    return HttpStatus.INTERNAL_SERVER_ERROR;
                }

                return HttpStatus.BAD_REQUEST;
        }
    }
}
