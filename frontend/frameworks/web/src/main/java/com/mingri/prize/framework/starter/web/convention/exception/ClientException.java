package com.mingri.prize.framework.starter.web.convention.exception;

import com.mingri.prize.framework.starter.web.convention.errorcode.BaseErrorCode;
import com.mingri.prize.framework.starter.web.convention.errorcode.IErrorCode;
import lombok.ToString;

/**
 * 客户端异常
 */
@ToString
public class ClientException extends AbstractException {

    public ClientException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ClientException(String message) {
        this(message, null, BaseErrorCode.CLIENT_ERROR);
    }

    public ClientException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ClientException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }
}
