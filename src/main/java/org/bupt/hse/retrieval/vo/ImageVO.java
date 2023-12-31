package org.bupt.hse.retrieval.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-01
 */
@ApiModel("ImageVO")
public class ImageVO {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("生成文件名")
    private String fileName;

    @ApiModelProperty("原文件名")
    private String originName;

    @ApiModelProperty("上传时间")
    private LocalDateTime uploadTime;

    @ApiModelProperty("图片类型")
    private String imgType;

    @ApiModelProperty("上传用户id")
    private String publisher;

    @ApiModelProperty("上传用户昵称")
    private String userName;

    @ApiModelProperty("下载地址")
    private String address;

    @ApiModelProperty("图片描述")
    private String description;

    @ApiModelProperty("是否收藏")
    private boolean star;

    @ApiModelProperty("被收藏数量")
    private String starCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public String getStarCount() {
        return starCount;
    }

    public void setStarCount(String starCount) {
        this.starCount = starCount;
    }
}
