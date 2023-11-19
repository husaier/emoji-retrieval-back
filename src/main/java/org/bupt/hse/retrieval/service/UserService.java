package org.bupt.hse.retrieval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.bupt.hse.retrieval.entity.UserDO;
import org.bupt.hse.retrieval.params.LoginParam;
import org.bupt.hse.retrieval.params.RegisterParam;
import org.bupt.hse.retrieval.vo.UserVO;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-11-19
 */
public interface UserService extends IService<UserDO> {

    UserVO login(LoginParam param);

    UserVO register(RegisterParam param);
}
