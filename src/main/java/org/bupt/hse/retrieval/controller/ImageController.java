package org.bupt.hse.retrieval.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.bupt.hse.retrieval.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Hu Saier <husaier@bupt.edu.cn>
 * Created on 2022-03-20
 */
@RestController
@RequestMapping("api/img")
@Api(tags = "图片接口")
@Slf4j
public class ImageController {


    private String classPath = "/home/hse/projects/emoji-retrieval-back-end/tmp";

    @GetMapping(value = "download/{img}")
    @ApiOperation(value = "下载图片")
    public ResponseEntity<InputStreamResource> downloadImage(@PathVariable(name = "img")
                                                             @ApiParam(value = "file_name", required = true) String img) throws IOException {

        String suffixName = img.substring(img.lastIndexOf("."));
        String path = String.format("%s/emoji/%s", classPath, img);
        FileSystemResource resource = new FileSystemResource(path);
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
    }

//    @PostMapping("upload/{tweetId}")
//    @ApiOperation(value = "上传图片")
//    public Result<String> uploadImage(@ApiParam(value = "上传的jpg文件", required = true)
//                                      @RequestPart("file") MultipartFile file,
//                                      @PathVariable(name = "tweetId")
//                                      @ApiParam(value = "推特ID", required = true) Long tweetId) {
//        if (file.isEmpty()) {
//            return Result.failed("文件不能为空");
//        }
//        String originalName = file.getOriginalFilename();
//        if (originalName == null || originalName.isEmpty()) {
//            return Result.failed("文件名不能为空");
//        }
//        String suffixName = originalName.substring(originalName.lastIndexOf("."));
//        ImageTypeEnum imageType = ImageTypeEnum.parseTypeBySuffix(suffixName);
//        if (imageType == null) {
//            return Result.failed("图片格式错误");
//        }
//        String imgName = UUID.randomUUID() + suffixName;
//        if (!imageService.checkAndSaveImg(tweetId, originalName, imgName, imageType)) {
//            return Result.failed("上传失败，数据库保存失败");
//        }
//        try {
//            String path = String.format("%s/img/%d/%s", classPath, tweetId, imgName);
//            File newFile = new File(path);
//            FileUtils.touch(newFile);
//            file.transferTo(newFile);
//            log.info("{} 保存成功", path);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.failed("上传失败！");
//        }
//        return Result.success("上传成功！");
//    }
}
