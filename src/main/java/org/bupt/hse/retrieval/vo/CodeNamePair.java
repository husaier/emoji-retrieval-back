package org.bupt.hse.retrieval.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Hu Saier <husaier@bupt.edu.cn>
 * Created on 2022-04-22
 */
@ApiModel("情感类别")
public class CodeNamePair {

    @ApiModelProperty(value = "编码")
    String code;

    @ApiModelProperty(value = "名称")
    String name;

    public CodeNamePair(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}
