package org.bupt.hse.retrieval.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-10
 */
@ApiModel("ImageUploadVO")
public class BatchUploadImageVO {

    @ApiModelProperty("上传成功图片数量")
    private int successCount;

    @ApiModelProperty("上传失败数量")
    private int failCount;

    @ApiModelProperty("上传图片总数")
    private int total;

    @ApiModelProperty("图片id列表")
    private List<String> idList;

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
}
