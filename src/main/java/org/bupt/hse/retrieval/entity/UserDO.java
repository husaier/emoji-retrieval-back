package org.bupt.hse.retrieval.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Hu Saier
 * @since 2023-11-19
 */
@ApiModel(value = "User对象", description = "")
@TableName("user")
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户类型，0为普通用户，1为管理员用户")
    private String userType;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("注册时间戳")
    private String timestamp;

    @ApiModelProperty("加密密码")
    private String pwdEncrypt;

    @ApiModelProperty("逻辑删除")
    @TableLogic(value = "0", delval = "1")
    @TableField(value = "`delete`")
    private Byte delete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPwdEncrypt() {
        return pwdEncrypt;
    }

    public void setPwdEncrypt(String pwdEncrypt) {
        this.pwdEncrypt = pwdEncrypt;
    }

    public Byte getDelete() {
        return delete;
    }

    public void setDelete(Byte delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return "User{" +
        "id = " + id +
        ", userType = " + userType +
        ", name = " + name +
        ", email = " + email +
        ", timestamp = " + timestamp +
        ", pwdEncrypt = " + pwdEncrypt +
        ", delete = " + delete +
        "}";
    }
}
