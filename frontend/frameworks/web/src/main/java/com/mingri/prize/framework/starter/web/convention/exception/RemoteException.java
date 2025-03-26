package com.mingri.prize.framework.starter.web.convention.exception;

import com.mingri.prize.framework.starter.web.convention.errorcode.BaseErrorCode;
import com.mingri.prize.framework.starter.web.convention.errorcode.IErrorCode;
import lombok.ToString;

/**
 * 远程服务调用异常
 */
@ToString
public class RemoteException extends AbstractException {

    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }

    public RemoteException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }
}
