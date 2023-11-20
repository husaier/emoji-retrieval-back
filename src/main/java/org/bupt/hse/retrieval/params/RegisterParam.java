package org.bupt.hse.retrieval.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-19
 */
@Data
@ApiModel("注册参数")
public class RegisterParam {
    @ApiModelProperty(value = "昵称", required = true)
    private String name;

    @ApiModelProperty(value = "密码", required = true)
    private String pwd;

    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
}
