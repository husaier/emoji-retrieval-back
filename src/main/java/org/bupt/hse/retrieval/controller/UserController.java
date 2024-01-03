package org.bupt.hse.retrieval.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.common.Result;
import org.bupt.hse.retrieval.enums.BizExceptionEnum;
import org.bupt.hse.retrieval.params.LoginParam;
import org.bupt.hse.retrieval.params.RegisterParam;
import org.bupt.hse.retrieval.service.UserService;
import org.bupt.hse.retrieval.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by Saier Hu
 * email <husserl@bupt.edu.cn>
 * 2023-10-19
 */
@RestController
@RequestMapping("api/user")
@Api(tags = "用户管理接口")
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "login")
    @ApiOperation(value = "登陆")
    public Result<UserVO> login(@RequestBody
                                LoginParam param) {
        UserVO vo = userService.login(param);
        if (vo == null) {
            return Result.failed(BizExceptionEnum.WRONG_PASS_WORD.getMsg());
        }
        return Result.success(vo);
    }

    @PostMapping(value = "register")
    @ApiOperation(value = "注册")
    public Result<UserVO> register(@RequestBody
                                   RegisterParam param) {
        try {
            UserVO vo = userService.register(param);
            return Result.success(vo);
        } catch (BizException e) {
            return Result.failed(e.getMsg());
        }
    }
}
