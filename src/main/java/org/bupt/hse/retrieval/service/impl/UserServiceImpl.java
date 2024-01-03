package org.bupt.hse.retrieval.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.common.Constants;
import org.bupt.hse.retrieval.entity.UserDO;
import org.bupt.hse.retrieval.enums.BizExceptionEnum;
import org.bupt.hse.retrieval.enums.UserTypeEnums;
import org.bupt.hse.retrieval.infra.UserInfraService;
import org.bupt.hse.retrieval.params.LoginParam;
import org.bupt.hse.retrieval.params.RegisterParam;
import org.bupt.hse.retrieval.service.UserService;
import org.bupt.hse.retrieval.utils.MD5Utils;
import org.bupt.hse.retrieval.utils.RedisUtil;
import org.bupt.hse.retrieval.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-19
 */
@Service
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private UserInfraService userInfraService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public UserVO login(LoginParam param) {
        log.info(JSON.toJSONString(param));
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getEmail, param.getEmail());
        UserDO userDO = userInfraService.getOne(queryWrapper);
        String pwdEncrypt = userDO.getPwdEncrypt();
        String timestamp = userDO.getTimestamp();
        if (MD5Utils.valid(pwdEncrypt, param.getPwd(), timestamp)) {
            HttpSession session = httpRequest.getSession();
            session.setAttribute(Constants.SESSION_USER_ID, userDO.getId());
            UserVO vo = new UserVO();
            vo.setId(userDO.getId());
            vo.setName(userDO.getName());
            vo.setEmail(userDO.getEmail());
            return vo;
        } else {
            return null;
        }
    }

    @Override
    public UserVO register(RegisterParam param) throws BizException {
        checkRegister(param);

        UserDO userDO = new UserDO();
        userDO.setUserType(UserTypeEnums.MANAGER.getCode());
        userDO.setName(param.getName());
        userDO.setEmail(param.getEmail());
        String timestamp = String.valueOf(System.currentTimeMillis());
        userDO.setTimestamp(timestamp);
        userDO.setPwdEncrypt(MD5Utils.MD5Lower(param.getPwd(), timestamp));
        userInfraService.save(userDO);

        UserVO vo = new UserVO();
        vo.setId(userDO.getId());
        vo.setName(userDO.getName());
        vo.setEmail(userDO.getEmail());
        return vo;
    }

    @Override
    public UserDO getCurUserInfo() {
        HttpSession session = httpRequest.getSession();
        Long userId = (Long) session.getAttribute(Constants.SESSION_USER_ID);
        return userInfraService.getById(userId);
    }

    @Override
    public List<UserDO> getUserInfo() {
        return userInfraService.list();
    }

    @Override
    public List<String> getLikeList() throws BizException {
        UserDO userDO = getCurUserInfo();
        Long userId = userDO.getId();
        Set<Object> objs = redisUtil.members(String.valueOf(userId));
        return objs.stream()
                .map(x -> {
                    String s = (String) x;
                    return s;
                }).collect(Collectors.toList());
    }

    @Override
    public Set<Long> getLikeSet(Long userId) {
        Set<Object> objs = redisUtil.members(String.valueOf(userId));
        return objs.stream()
                .map(x -> {
            String s = (String) x;
            return Long.parseLong(s);
        }).collect(Collectors.toSet());
    }

    private void checkRegister(RegisterParam param) throws BizException {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getEmail, param.getEmail());
        UserDO user = userInfraService.getOne(queryWrapper);
        if (user != null) {
            throw new BizException(BizExceptionEnum.EMAIL_ALREADY_REGISTERED);
        }
    }
}
