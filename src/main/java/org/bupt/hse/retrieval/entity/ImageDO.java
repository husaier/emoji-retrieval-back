package org.bupt.hse.retrieval.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Hu Saier
 * @since 2023-11-22
 */
@ApiModel(value = "Image对象", description = "")
@TableName("image")
public class ImageDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("生成文件名")
    private String fileName;

    @ApiModelProperty("原文件名")
    private String originName;

    @ApiModelProperty("上传时间")
    private LocalDateTime uploadTime;

    @ApiModelProperty("图片类型")
    private String imgType;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("逻辑删除，1表示已删除，0表示未删除")
    @TableLogic(value = "0", delval = "1")
    private Byte deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Image{" +
        "id = " + id +
        ", fileName = " + fileName +
        ", uploadTime = " + uploadTime +
        ", imgType = " + imgType +
        ", deleted = " + deleted +
        "}";
    }
}
