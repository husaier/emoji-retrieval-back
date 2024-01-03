package org.bupt.hse.retrieval.infra;

import com.baomidou.mybatisplus.extension.service.IService;
import org.bupt.hse.retrieval.entity.ImageDO;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-04
 */
public interface ImageInfraService extends IService<ImageDO> {

    void hardDeleteImage(Long imgId);
}
