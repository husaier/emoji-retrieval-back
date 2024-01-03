package org.bupt.hse.retrieval.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-11-19
 */
@ApiModel("用户信息")
public class UserVO {

    @ApiModelProperty("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private long id;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("邮箱")
    private String email;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
