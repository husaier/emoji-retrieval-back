package org.bupt.hse.retrieval.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Hu Saier <husaier@bupt.edu.cn>
 * Created on 2022-04-22
 */
@Data
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
}
