package org.bupt.hse.retrieval.enums;


import org.bupt.hse.retrieval.common.IErrorCode;

/**
 * 枚举了一些常用API操作码
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(20000, "操作成功","ok"),
    FAILED(500, "操作失败","error"),
    VALIDATE_FAILED(404, "参数检验失败","error"),
    UNAUTHORIZED(50014, "暂未登录或token已经过期","error"),
    FORBIDDEN(50008, "没有相关权限","error");

    private long code;
    private String message;
    private String status;

    private ResultCode(long code, String message, String status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
