package org.bupt.hse.retrieval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.entity.UserDO;
import org.bupt.hse.retrieval.params.LoginParam;
import org.bupt.hse.retrieval.params.RegisterParam;
import org.bupt.hse.retrieval.vo.UserVO;

import java.util.List;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-19
 */
public interface UserService extends IService<UserDO> {

    /**
     * 用户登录
     * @param param
     * @return
     */
    UserVO login(LoginParam param);

    /**
     * 用户注册
     * @param param
     * @return
     * @throws BizException
     */
    UserVO register(RegisterParam param) throws BizException;

    /**
     * 获取当前登录用户信息
     * @return
     */
    UserDO getUserDO();

    List<UserDO> getUserInfo();
}
