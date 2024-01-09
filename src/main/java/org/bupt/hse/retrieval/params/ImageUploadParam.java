package org.bupt.hse.retrieval.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-06
 */
@ApiModel("图片上传参数")
public class ImageUploadParam {

    @ApiModelProperty(value = "图片文件id",
            required = true)
    private String fileId;

    @ApiModelProperty(value = "图片描述",
            required = true)
    private String description;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
