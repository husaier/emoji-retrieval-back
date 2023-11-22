package org.bupt.hse.retrieval.common;

import org.bupt.hse.retrieval.enums.BizExceptionEnum;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-11-19
 */
public class BizException extends Exception {

    private final String code;
    private final String msg;

    public BizException(BizExceptionEnum bizExceptionEnum) {
        this.code = bizExceptionEnum.getCode();
        this.msg = bizExceptionEnum.getMsg();
    }

    public BizException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
