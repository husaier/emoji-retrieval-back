package org.bupt.hse.retrieval.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.io.FileUtils;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.entity.ImageDO;
import org.bupt.hse.retrieval.entity.UserDO;
import org.bupt.hse.retrieval.enums.BizExceptionEnum;
import org.bupt.hse.retrieval.enums.ImageTypeEnum;
import org.bupt.hse.retrieval.infra.ImageInfraService;
import org.bupt.hse.retrieval.params.ImageEditParam;
import org.bupt.hse.retrieval.params.ImageUploadParam;
import org.bupt.hse.retrieval.service.ImageService;
import org.bupt.hse.retrieval.service.UserService;
import org.bupt.hse.retrieval.utils.MyFileUtils;
import org.bupt.hse.retrieval.utils.RedisUtil;
import org.bupt.hse.retrieval.utils.RestTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hu Saier
 * @since 2023-11-22
 */
@Service
public class ImageServiceImpl implements ImageService {

    private final static Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Value("${file.storage.class-path}")
    private String classPath;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ImageInfraService imageInfraService;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Override
    public FileSystemResource downloadImage(Long imgId) throws BizException {
        ImageDO imageDO = imageInfraService.getById(imgId);
        if (imageDO == null) {
            throw new BizException(BizExceptionEnum.INVALID_IMG_ID);
        }
        String fileName = imageDO.getFileName();
        String path = String.format("%s/emoji/%s", classPath, fileName);
        return new FileSystemResource(path);
    }

    @Override
    public Long uploadImage(MultipartFile file, ImageUploadParam param) throws BizException {
        UserDO userDO = userService.getCurUserInfo();
        if (file.isEmpty()) {
            throw new BizException(BizExceptionEnum.EMPTY_FILE);
        }
        String originalName = file.getOriginalFilename();
        assert originalName != null;
        String suffixName = originalName.substring(originalName.lastIndexOf("."));
        ImageTypeEnum imageType = ImageTypeEnum.parseTypeBySuffix(suffixName);
        if (imageType == null) {
            throw new BizException(BizExceptionEnum.INVALID_FILE_TYPE);
        }
        ImageDO imageDO = new ImageDO();
        imageDO.setOriginName(originalName);
        imageDO.setImgType(imageType.getCode());
        imageDO.setUploadTime(LocalDateTime.now());
        imageDO.setEditTime(LocalDateTime.now());
        imageDO.setPublisher(userDO.getId());
        imageDO.setDescription(param.getDescription());
        imageInfraService.save(imageDO);
        String imgName = String.format("%d%s", imageDO.getId(), suffixName);
        try {
            String path = String.format("%s/emoji/%s", classPath, imgName);
            File newFile = new File(path);
            FileUtils.touch(newFile);
            file.transferTo(newFile);
            imageDO.setFileName(imgName);
            imageInfraService.saveOrUpdate(imageDO);
        } catch (Exception e) {
            throw new BizException(BizExceptionEnum.FILE_UPLOAD_FAIL);
        }
        Map<String, String> processParam = new HashMap<>();
        processParam.put("id", String.valueOf(imageDO.getId()));
        processParam.put("file_name", imgName);
        String paramStr = JSON.toJSONString(processParam);
        Map<String, String> headers = new HashMap<>();
        JSONObject res = restTemplateUtil.post("http://127.0.0.1:8002/process/image", paramStr, headers);
        if (res.getInteger("code") != 200) {
            log.error(String.format("图像嵌入生成失败，接口返回res=%s", JSON.toJSONString(res)));
            throw new BizException(BizExceptionEnum.FAIL_CREATE_IMAGE_EMBEDDING);
        }
        imageDO.setHasEmbedding(true);
        imageInfraService.saveOrUpdate(imageDO);
        return imageDO.getId();
    }

    @Override
    public void deleteImage(Long imgId) throws BizException {
        UserDO userDO = userService.getCurUserInfo();
        ImageDO imageDO = imageInfraService.getById(imgId);
        if (imageDO == null) {
            throw new BizException(BizExceptionEnum.INVALID_IMG_ID);
        }
        List<UserDO> userInfo = userService.getAllUserInfo();
        userInfo.forEach(x -> {
            String userKey = String.format("user_%d", x.getId());
            redisUtil.remove(userKey, String.valueOf(imgId));
        });
        imageInfraService.removeById(imgId);
    }

    @Override
    public void hardDeleteImage(Long imgId) throws BizException {
        UserDO userDO = userService.getCurUserInfo();
        ImageDO imageDO = imageInfraService.getById(imgId);
        if (imageDO == null) {
            throw new BizException(BizExceptionEnum.INVALID_IMG_ID);
        }
        String fileName = imageDO.getFileName();
        String path = String.format("%s/emoji/%s", classPath, fileName);
        boolean res = MyFileUtils.deleteFile(path);
        if (!res) {
            throw new BizException(BizExceptionEnum.FILE_DELETE_FAIL);
        }
        List<UserDO> userInfo = userService.getAllUserInfo();
        userInfo.forEach(x -> {
            String userKey = String.format("user_%d", x.getId());
            redisUtil.remove(userKey, String.valueOf(imgId));
        });
        imageInfraService.hardDeleteImage(imgId);
    }


    @Override
    public void likeImage(Long imgId) throws BizException {
        ImageDO imageDO = imageInfraService.getById(imgId);
        if (imageDO == null) {
            throw new BizException(BizExceptionEnum.INVALID_IMG_ID);
        }
        UserDO userDO = userService.getCurUserInfo();
        Long userId = userDO.getId();
        String userKey = String.format("user_%d", userId);
        redisUtil.sSet(userKey, String.valueOf(imgId));
        imageDO.setStarCount(imageDO.getStarCount() + 1);
        imageInfraService.updateById(imageDO);
    }

    @Override
    public void unlikeImage(Long imgId) throws BizException {
        ImageDO imageDO = imageInfraService.getById(imgId);
        if (imageDO == null) {
            throw new BizException(BizExceptionEnum.INVALID_IMG_ID);
        }
        UserDO userDO = userService.getCurUserInfo();
        Long userId = userDO.getId();
        String userKey = String.format("user_%d", userId);
        redisUtil.remove(userKey, String.valueOf(imgId));
        imageDO.setStarCount(imageDO.getStarCount() - 1);
        imageInfraService.updateById(imageDO);
    }

    @Override
    public void editDescription(ImageEditParam param) throws BizException {
        Long imgId = param.getImgId();
        ImageDO imageDO = imageInfraService.getById(imgId);
        if (imageDO == null) {
            throw new BizException(BizExceptionEnum.INVALID_IMG_ID);
        }
        UserDO userDO = userService.getCurUserInfo();
        Long id = userDO.getId();
        if (!Objects.equals(id, imageDO.getPublisher())) {
            throw new BizException(BizExceptionEnum.NO_AUTHORIZATION);
        }
        imageDO.setDescription(param.getDescription());
        imageDO.setEditTime(LocalDateTime.now());
        imageInfraService.updateById(imageDO);
    }

    @Override
    public long countImgStars(Long imgId) {
        ImageDO imageDO = imageInfraService.getById(imgId);
        if (imageDO == null) {
            return 0;
        } else {
            return imageDO.getStarCount();
        }
    }

    @Override
    public void createEmbedding() throws BizException {
        LambdaQueryWrapper<ImageDO> queryWrapper = new LambdaQueryWrapper<ImageDO>()
                .eq(ImageDO::getHasEmbedding, false);
        long total = imageInfraService.count(queryWrapper);
        long pages = total / 10;
        if (total % 10 != 0) {
            pages++;
        }
        long count = 0;
        for (int i = 1; i <= pages; i++) {
            Page<ImageDO> page = new Page<>(i, 10);
            Page<ImageDO> res = imageInfraService.page(page, queryWrapper);
            for (ImageDO itm : res.getRecords()) {
                Map<String, String> processParam = new HashMap<>();
                processParam.put("id", String.valueOf(itm.getId()));
                processParam.put("file_name", itm.getFileName());
                String paramStr = JSON.toJSONString(processParam);
                Map<String, String> headers = new HashMap<>();
                JSONObject response = restTemplateUtil.post("http://127.0.0.1:8002/process/image", paramStr, headers);
                if (response.getInteger("code") != 200) {
                    log.error(String.format("图像嵌入生成失败，接口返回res=%s", JSON.toJSONString(res)));
                    throw new BizException(BizExceptionEnum.FAIL_CREATE_IMAGE_EMBEDDING);
                }
                itm.setHasEmbedding(true);
                imageInfraService.updateById(itm);
                count++;
                log.info(String.format("已生成/总量：%d/%d，id = %d", count, total, itm.getId()));
            }
        }
    }
}
