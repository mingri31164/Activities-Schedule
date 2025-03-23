package com.mingri.prize.api.action;

import com.mingri.prize.commons.db.service.CardUserService;
import com.mingri.prize.commons.utils.ApiResult;
import com.mingri.prize.commons.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api")
@Api(tags = {"登录模块"})
public class LoginController {
    @Autowired
    private CardUserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="account",value = "用户名",required = true),
            @ApiImplicitParam(name="password",value = "密码",required = true)
    })
    public ApiResult login(HttpServletRequest request, @RequestParam String account,@RequestParam String password) {
        ApiResult accountLogin = userService.login(request,account,password);
        // 登录成功,将用户信息到存入session
        HttpSession session = request.getSession();
        session.setAttribute("user", accountLogin.getData());
        return accountLogin;
    }

    @GetMapping("/logout")
    @ApiOperation(value = "退出")
    public ApiResult logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); //清除session
        }
        return new ApiResult(1,"退出成功",null);
    }
}