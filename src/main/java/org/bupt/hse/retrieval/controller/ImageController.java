package org.bupt.hse.retrieval.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.common.Result;
import org.bupt.hse.retrieval.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Hu Saier <husaier@bupt.edu.cn>
 * Created on 2022-03-20
 */
@RestController
@RequestMapping("api/img")
@Api(tags = "图片管理接口")
public class ImageController {

    private final static Logger log = LoggerFactory.getLogger(ImageController.class);

    @Value("${file.storage.class-path}")
    private String classPath;

    @Autowired
    private ImageService imageService;

    @GetMapping(value = "download/{imgId}")
    @ApiOperation(value = "下载图片")
    public ResponseEntity<InputStreamResource> downloadImage(@PathVariable(name = "imgId")
                                                             @ApiParam(value = "image id", required = true)
                                                             Long imgId) {
        try {
            FileSystemResource resource = imageService.downloadImage(imgId);
            String name = resource.getFilename();
            assert name != null;
            String suffixName = name.substring(name.lastIndexOf("."));
            if (suffixName.equals(".jpg")) {
                return ResponseEntity
                        .ok()
                        .contentLength(resource.contentLength())
                        .contentType(MediaType.parseMediaType("image/jpeg"))
                        .body(new InputStreamResource(resource.getInputStream()));
            } else {
                return ResponseEntity
                        .ok()
                        .contentLength(resource.contentLength())
                        .contentType(MediaType.parseMediaType("image/gif"))
                        .body(new InputStreamResource(resource.getInputStream()));
            }
        } catch (IOException e) {
            InputStream stream = new ByteArrayInputStream(
                    "图片读取错误".getBytes(StandardCharsets.UTF_8));
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new InputStreamResource(stream));
        } catch (BizException e) {
            InputStream stream = new ByteArrayInputStream(
                    e.getMsg().getBytes(StandardCharsets.UTF_8));
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new InputStreamResource(stream));
        }
    }

    @PostMapping("upload")
    @ApiOperation(value = "上传图片")
    public Result<String> uploadImage(@ApiParam(value = "上传的jpg文件", required = true)
                                      @RequestPart("file")
                                      MultipartFile file) {
        try {
            Long imgId = imageService.uploadImage(file);
            return Result.success(String.valueOf(imgId));
        } catch (BizException e) {
            return Result.failed(e.getMsg());
        }
    }

    @DeleteMapping("delete/{imgId}")
    @ApiOperation(value = "删除图片")
    public Result<String> deleteImage(@PathVariable(name = "imgId")
                                      @ApiParam(value = "image id", required = true)
                                      Long imgId) {
        try {
            imageService.deleteImage(imgId);
            return Result.success("删除成功！");
        } catch (BizException e) {
            return Result.failed(e.getMsg());
        }
    }

    @PutMapping("like/{imgId}")
    @ApiOperation(value = "收藏图片")
    public Result<String> likeImage(@PathVariable(name = "imgId")
                                    @ApiParam(value = "image id", required = true)
                                    Long imgId) {
        try {
            imageService.likeImage(imgId);
            return Result.success("收藏成功！");
        } catch (BizException e){
            return Result.failed(e.getMsg());
        }
    }

    @PutMapping("unlike/{imgId}")
    @ApiOperation(value = "取消收藏图片")
    public Result<String> unlikeImage(@PathVariable(name = "imgId")
                                    @ApiParam(value = "image id", required = true)
                                    Long imgId) {
        try {
            imageService.unlikeImage(imgId);
            return Result.success("取消收藏成功！");
        } catch (BizException e){
            return Result.failed(e.getMsg());
        }
    }
}
