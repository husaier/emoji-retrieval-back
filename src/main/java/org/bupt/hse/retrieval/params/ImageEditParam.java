package org.bupt.hse.retrieval.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-04
 */
@ApiModel("图片编辑参数")
public class ImageEditParam {

    @ApiModelProperty(value = "图片id",
            required = true)
    private Long imgId;

    @ApiModelProperty(value = "图片描述",
            required = true)
    private String description;

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
