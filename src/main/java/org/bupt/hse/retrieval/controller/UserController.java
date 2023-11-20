package org.bupt.hse.retrieval.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.common.Result;
import org.bupt.hse.retrieval.params.LoginParam;
import org.bupt.hse.retrieval.params.RegisterParam;
import org.bupt.hse.retrieval.service.UserService;
import org.bupt.hse.retrieval.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "login")
    @ApiOperation(value = "登陆")
    public Result<UserVO> login(@RequestBody LoginParam param) {
        UserVO vo = userService.login(param);
        if (vo == null) {
            return Result.failed();
        }
        return Result.success(vo);
    }

    @PostMapping(value = "register")
    @ApiOperation(value = "注册")
    public Result<UserVO> register(@RequestBody RegisterParam param) {
        try {
            UserVO vo = userService.register(param);
            return Result.success(vo);
        } catch (BizException e) {
            return Result.failed(e.getMessage());
        }
    }
}
