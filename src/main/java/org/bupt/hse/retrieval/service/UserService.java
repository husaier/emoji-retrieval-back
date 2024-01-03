package org.bupt.hse.retrieval.service;

import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.entity.UserDO;
import org.bupt.hse.retrieval.params.LoginParam;
import org.bupt.hse.retrieval.params.RegisterParam;
import org.bupt.hse.retrieval.vo.UserVO;

import java.util.List;
import java.util.Set;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-19
 */
public interface UserService {

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
    UserDO getCurUserInfo();

    /**
     * 获取系统中所有用户信息
     * @return
     */
    List<UserDO> getUserInfo();

    /**
     * 获得当前用户收藏列表，imgId
     * @param
     * @return
     */
    List<String> getLikeList() throws BizException;

    /**
     * 获得指定用户收藏集合，imgId
     * @param userId
     * @return
     */
    Set<Long> getLikeSet(Long userId);
}
