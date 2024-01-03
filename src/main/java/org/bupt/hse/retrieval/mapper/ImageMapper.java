package org.bupt.hse.retrieval.mapper;

import org.bupt.hse.retrieval.entity.ImageDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hu Saier
 * @since 2023-11-22
 */
public interface ImageMapper extends BaseMapper<ImageDO> {
    void hardDeleteImage(Long imgId);
}
