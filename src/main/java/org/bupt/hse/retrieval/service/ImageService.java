package org.bupt.hse.retrieval.service;

import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.params.ImageEditParam;
import org.bupt.hse.retrieval.params.ImageUploadParam;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hu Saier
 * @since 2023-11-22
 */
public interface ImageService {

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
    Long uploadImage(MultipartFile file, ImageUploadParam param) throws BizException;

    /**
     * 批量上传图片
     * @param files
     * @return
     */
    List<Long> batchUploadImage(MultipartFile[] files);

    /**
     * 逻辑删除图片
     * @param imgId
     * @throws BizException
     */
    void deleteImage(Long imgId) throws BizException;

    /**
     * 硬删除图片
     * @param imgId
     * @throws BizException
     */
    void hardDeleteImage(Long imgId) throws BizException;

    /**
     * 收藏图片
     * @param imgId
     * @throws BizException
     */
    void likeImage(Long imgId) throws BizException;

    /**
     * 取消收藏图片
     * @param imgId
     * @throws BizException
     */
    void unlikeImage(Long imgId) throws BizException;

    /**
     * 编辑图片描述
     * @param param
     * @throws BizException
     */
    void editDescription(ImageEditParam param) throws BizException;

    /**
     * 查询一张图片的被收藏数量
     * @param imgId
     * @return
     * @throws BizException
     */
    long countImgStars(Long imgId);

    /**
     * 生成数据库中所有的图片的嵌入
     */
    void createEmbedding() throws BizException;
}
