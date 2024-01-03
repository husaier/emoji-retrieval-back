package org.bupt.hse.retrieval.infra.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bupt.hse.retrieval.entity.ImageDO;
import org.bupt.hse.retrieval.infra.ImageInfraService;
import org.bupt.hse.retrieval.mapper.ImageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-04
 */
@Service
public class ImageInfraServiceImpl extends ServiceImpl<ImageMapper, ImageDO> implements ImageInfraService {

    private final static Logger log = LoggerFactory.getLogger(ImageInfraServiceImpl.class);

    @Value("${file.storage.class-path}")
    private String classPath;


    @Override
    public void hardDeleteImage(Long imgId) {
        this.baseMapper.hardDeleteImage(imgId);
    }
}
