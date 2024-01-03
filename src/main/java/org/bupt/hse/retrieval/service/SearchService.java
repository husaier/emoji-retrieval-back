package org.bupt.hse.retrieval.service;

import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.vo.ImageVO;
import org.bupt.hse.retrieval.vo.PageVO;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-01
 */
public interface SearchService {

    PageVO<ImageVO> searchImages(long cur, long pageSize) throws BizException;
}
