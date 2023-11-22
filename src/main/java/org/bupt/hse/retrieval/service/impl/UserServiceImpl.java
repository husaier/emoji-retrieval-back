package org.bupt.hse.retrieval.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.common.Constants;
import org.bupt.hse.retrieval.common.MD5Utils;
import org.bupt.hse.retrieval.entity.UserDO;
import org.bupt.hse.retrieval.enums.BizExceptionEnum;
import org.bupt.hse.retrieval.enums.UserTypeEnums;
import org.bupt.hse.retrieval.mapper.UserMapper;
import org.bupt.hse.retrieval.params.LoginParam;
import org.bupt.hse.retrieval.params.RegisterParam;
import org.bupt.hse.retrieval.service.UserService;
import org.bupt.hse.retrieval.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-19
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    @Autowired
    private HttpServletRequest httpRequest;

    @Override
    public UserVO login(LoginParam param) {
        log.info(JSON.toJSONString(param));
        UserDO userDO = this.getOne(new LambdaQueryWrapper<UserDO>().eq(UserDO::getEmail, param.getEmail()));
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
        this.save(userDO);

        UserVO vo = new UserVO();
        vo.setId(userDO.getId());
        vo.setName(userDO.getName());
        vo.setEmail(userDO.getEmail());
        return vo;
    }

    @Override
    public UserDO getUserDO() {
        HttpSession session = httpRequest.getSession();
        Long userId = (Long) session.getAttribute(Constants.SESSION_USER_ID);
        return getById(userId);
    }

    private void checkRegister(RegisterParam param) throws BizException {
        UserDO user = this.getOne(new LambdaQueryWrapper<UserDO>().eq(UserDO::getEmail, param.getEmail()));
        if (user != null) {
            throw new BizException(BizExceptionEnum.EMAIL_ALREADY_REGISTERED);
        }
    }
}
