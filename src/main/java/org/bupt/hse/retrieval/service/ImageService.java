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

    /**
     * 下载图片
     * @param imgId
     * @return
     * @throws BizException
     */
    FileSystemResource downloadImage(Long imgId) throws BizException;

    /**
     * 上传图片
     * @param file
     * @return
     * @throws BizException
     */
    Long uploadImage(MultipartFile file) throws BizException;

    /**
     * 逻辑删除文件
     * @param imgId
     * @throws BizException
     */
    void deleteImage(Long imgId) throws BizException;
}
