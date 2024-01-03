package org.bupt.hse.retrieval.infra.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bupt.hse.retrieval.entity.UserDO;
import org.bupt.hse.retrieval.infra.UserInfraService;
import org.bupt.hse.retrieval.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-04
 */
@Service
public class UserInfraServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserInfraService {
}
