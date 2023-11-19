package org.bupt.hse.retrieval.common;

/**
 * 封装API的错误码
 */
public interface IErrorCode {
    long getCode();

    String getMessage();

    String getStatus();
}
