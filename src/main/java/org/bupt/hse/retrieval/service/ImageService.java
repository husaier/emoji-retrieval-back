package org.bupt.hse.retrieval.service;

import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.entity.ImageDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hu Saier
 * @since 2023-11-22
 */
public interface ImageService extends IService<ImageDO> {

    FileSystemResource downloadImage(Long imgId) throws BizException;
    Long uploadImage(MultipartFile file) throws BizException;
}
