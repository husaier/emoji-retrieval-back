package org.bupt.hse.retrieval.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-19
 */
@Data
@ApiModel("登陆参数")
public class LoginParam {
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;

    @ApiModelProperty(value = "密码", required = true)
    private String pwd;
}
