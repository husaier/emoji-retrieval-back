package org.bupt.hse.retrieval.enums;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-19
 */
public enum BizExceptionEnum {
    EMAIL_ALREADY_REGISTERED("email_already_registered", "该邮箱已经注册"),
    INVALID_USER_NAME("invalid_user_name", "无效的昵称"),
    INVALID_EMAIL("invalid_email", "无效的邮箱地址"),
    INVALID_PWD("invalid_pwd", "无效的密码"),
    EMPTY_FILE("empty_file", "文件不能为空"),
    INVALID_FILE_TYPE("invalid_file_type", "图片格式错误"),
    FILE_UPLOAD_FAIL("file_upload_fail", "文件上传失败"),
    FILE_DELETE_FAIL("file_delete_fail", "文件删除失败"),
    INVALID_IMG_ID("invalid_img_id", "无效的图片ID"),
    INVALID_USER_TOKEN("invalid_user_token", "用户未登录，请登录"),
    WRONG_PASS_WORD("wrong_pass_word", "密码错误，请重试");

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
