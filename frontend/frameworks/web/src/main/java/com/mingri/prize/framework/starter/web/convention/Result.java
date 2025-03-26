package com.mingri.prize.framework.starter.web.convention;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 全局返回对象
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    /**
     * 正确返回码
     */
    public static final String SUCCESS_CODE = "1";
    /**
     * 失败返回码
     */
    public static final String FAILURE_CODE = "-1";

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 请求ID
     */
    private String requestId;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
}
