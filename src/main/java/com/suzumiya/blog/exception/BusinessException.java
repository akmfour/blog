package com.suzumiya.blog.exception;

import com.suzumiya.blog.common.ResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ResultCode resultCode;
    private final String detailMessage; // 附加详细信息

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.detailMessage = null;
    }

    // 有详细信息的构造函数
    public BusinessException(ResultCode resultCode, String detailMessage) {
        super(detailMessage != null && !detailMessage.isEmpty() ? detailMessage : resultCode.getMessage());
        this.resultCode = resultCode;
        this.detailMessage = detailMessage;
    }

    // 无详细信息的构造函数
    public BusinessException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
        this.detailMessage = null;
    }

}
