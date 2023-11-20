package org.bupt.hse.retrieval.enums;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-19
 */
public enum BizExceptionEnum {
    EMAIL_ALREADY_REGISTERED("email_already_registered", "该邮箱已经注册"),
    INVALID_USER_NAME("invalid_user_name", "无效的昵称"),
    INVALID_EMAIL("invalid_email", "无效的邮箱地址"),
    INVALID_PWD("invalid_pwd", "无效的密码");

    private final String code;
    private final String msg;

    BizExceptionEnum(String code, String msg) {
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
