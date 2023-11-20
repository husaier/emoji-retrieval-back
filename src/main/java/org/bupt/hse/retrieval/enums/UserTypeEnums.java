package org.bupt.hse.retrieval.enums;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-19
 */
public enum UserTypeEnums {
    NORMAL("normal", "普通用户"),
    MANAGER("manager", "管理员");

    private String code;
    private String name;

    UserTypeEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
