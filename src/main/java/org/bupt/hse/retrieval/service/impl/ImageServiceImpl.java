package org.bupt.hse.retrieval.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.io.FileUtils;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.entity.ImageDO;
import org.bupt.hse.retrieval.entity.UserDO;
import org.bupt.hse.retrieval.enums.BizExceptionEnum;
import org.bupt.hse.retrieval.enums.ImageTypeEnum;
import org.bupt.hse.retrieval.mapper.ImageMapper;
import org.bupt.hse.retrieval.service.ImageService;
import org.bupt.hse.retrieval.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hu Saier
 * @since 2023-11-22
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, ImageDO> implements ImageService {

    private final static Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Value("${file.storage.class-path}")
    private String classPath;

    @Autowired
    private UserService userService;

    @Override
    public FileSystemResource downloadImage(Long imgId) throws BizException {
        ImageDO imageDO = getById(imgId);
        if (imageDO == null) {
            throw new BizException(BizExceptionEnum.INVALID_IMG_ID);
        }
        String fileName = imageDO.getFileName();
        String path = String.format("%s/emoji/%s", classPath, fileName);
        return new FileSystemResource(path);
    }

    @Override
    public Long uploadImage(MultipartFile file) throws BizException {
        UserDO userDO = userService.getUserDO();
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
        imageDO.setUserId(userDO.getId());
        save(imageDO);
        String imgName = String.format("%d%s", imageDO.getId(), suffixName);
        try {
            String path = String.format("%s/emoji/%s", classPath, imgName);
            File newFile = new File(path);
            FileUtils.touch(newFile);
            file.transferTo(newFile);
            imageDO.setFileName(imgName);
            saveOrUpdate(imageDO);
        } catch (Exception e) {
            throw new BizException(BizExceptionEnum.FILE_UPLOAD_FAIL);
        }
        return imageDO.getId();
    }

    @Override
    public void deleteImage(Long imgId) throws BizException {
        UserDO userDO = userService.getUserDO();
        ImageDO imageDO = getById(imgId);
        if (imageDO == null) {
            throw new BizException(BizExceptionEnum.INVALID_IMG_ID);
        }
//        String fileName = imageDO.getFileName();
//        String path = String.format("%s/emoji/%s", classPath, fileName);
//        boolean res = MyFileUtils.deleteFile(path);
//        if (!res) {
//            throw new BizException(BizExceptionEnum.FILE_DELETE_FAIL);
//        }
        userService.removeById(userDO.getId());
    }
}
