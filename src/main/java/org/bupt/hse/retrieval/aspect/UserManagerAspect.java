package org.bupt.hse.retrieval.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.bupt.hse.retrieval.common.Result;
import org.bupt.hse.retrieval.enums.BizExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-22
 */
@Aspect
@Component
public class UserManagerAspect {
    @Autowired
    private HttpServletRequest httpRequest;

    @Pointcut(value = "execution(org.bupt.hse.retrieval.common.Result<*> org.bupt.hse.retrieval.controller.*.*(..)) " +
            "&& !execution(* org.bupt.hse.retrieval.controller.UserController.login(..))" +
            "&& !execution(* org.bupt.hse.retrieval.controller.UserController.register(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpSession session = httpRequest.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized(BizExceptionEnum.INVALID_USER_TOKEN.getMsg());
        }
        return proceedingJoinPoint.proceed();
    }
}
