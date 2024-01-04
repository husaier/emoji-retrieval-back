package org.bupt.hse.retrieval.service.impl;

import org.apache.commons.io.FileUtils;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.entity.ImageDO;
import org.bupt.hse.retrieval.entity.UserDO;
import org.bupt.hse.retrieval.enums.BizExceptionEnum;
import org.bupt.hse.retrieval.enums.ImageTypeEnum;
import org.bupt.hse.retrieval.infra.ImageInfraService;
import org.bupt.hse.retrieval.params.ImageEditParam;
import org.bupt.hse.retrieval.service.ImageService;
import org.bupt.hse.retrieval.service.UserService;
import org.bupt.hse.retrieval.utils.MyFileUtils;
import org.bupt.hse.retrieval.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
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
    public Long uploadImage(MultipartFile file) throws BizException {
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
        imageDO.setPublisher(userDO.getId());
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
        return imageDO.getId();
    }

    @Override
    public void deleteImage(Long imgId) throws BizException {
        UserDO userDO = userService.getCurUserInfo();
        ImageDO imageDO = imageInfraService.getById(imgId);
        if (imageDO == null) {
            throw new BizException(BizExceptionEnum.INVALID_IMG_ID);
        }
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
        imageInfraService.hardDeleteImage(imgId);
    }


    @Override
    public void likeImage(Long imgId) throws BizException {
        ImageDO imageDO = imageInfraService.getById(imgId);
        if (imageDO == null) {
            throw new BizException(BizExceptionEnum.INVALID_IMG_ID);
        }
        UserDO userDO = userService.getCurUserInfo();
        Long id = userDO.getId();
        redisUtil.sSet(String.valueOf(id), String.valueOf(imgId));
    }

    @Override
    public void unlikeImage(Long imgId) throws BizException {
        ImageDO imageDO = imageInfraService.getById(imgId);
        if (imageDO == null) {
            throw new BizException(BizExceptionEnum.INVALID_IMG_ID);
        }
        UserDO userDO = userService.getCurUserInfo();
        Long id = userDO.getId();
        redisUtil.remove(String.valueOf(id), String.valueOf(imgId));
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
}
