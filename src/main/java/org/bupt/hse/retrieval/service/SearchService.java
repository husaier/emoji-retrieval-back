package org.bupt.hse.retrieval.service;

import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.vo.ImageVO;
import org.bupt.hse.retrieval.vo.PageVO;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-01
 */
public interface SearchService {

    PageVO<ImageVO> getAllPage(long cur, long pageSize) throws BizException;

    /**
     * 分页查询所有图片的详细信息
     * @param cur
     * @param pageSize
     * @return
     * @throws BizException
     */
    PageVO<ImageVO> searchImages(String query, long cur, long pageSize) throws BizException;

    /**
     * 分页获取当前用户收藏图片详细信息
     * @param cur
     * @param pageSize
     * @return
     * @throws BizException
     */
    PageVO<ImageVO> getLikePage(long cur, long pageSize) throws BizException;

    /**
     * 分页获取当前用户上传的图片
     * @param cur
     * @param pageSize
     * @return
     * @throws BizException
     */
    PageVO<ImageVO> getUploadPage(long cur, long pageSize) throws BizException;
}
