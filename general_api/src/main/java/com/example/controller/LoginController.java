package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.example.annotations.SysLog;
import com.example.dao.SysUserDao;
import com.example.entity.SysUser;
import com.example.enums.CodeMsg;
import com.example.uitls.JwtUtil;
import com.example.uitls.ShiroUtils;
import com.example.utils.MyResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-06-24
 * Time: 15:05
 */
@RestController
@RequestMapping("/sys")
@Slf4j
public class LoginController {
    @Resource
    private SysUserDao sysUserDao;

    @PostMapping("/login")
    @SysLog()
    public MyResult login(String userName , String passWord){
        log.info("username:{},password:{}",userName,passWord);
        if(StrUtil.isBlank(userName) && StrUtil.isBlank(passWord)){
            return MyResult.error(CodeMsg.PARAMETER_ISNULL);
        }

        try {
            //获得当前用户到登录对象，现在状态为未认证
            Subject subject = ShiroUtils.getSubject();

            //用户名密码令牌
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName, passWord);

            //将令牌传到shiro提供的login方法验证，需要自定义realm
            subject.login(usernamePasswordToken);

            //登陆成功后 从shiro中获取用户信息
            SysUser sysUser = (SysUser) subject.getPrincipal();

            String jwt = JwtUtil.createToken(sysUser.getUserId());
            sysUser.setJwt(jwt);
            return MyResult.success(sysUser);

        }catch (UnknownAccountException uae) {
            log.warn("用户帐号不正确");
            throw uae;

        } catch (IncorrectCredentialsException ice) {
            log.warn("用户密码不正确");
            throw ice;

        } catch (LockedAccountException lae) {
            log.warn("用户帐号被锁定");
            throw lae;

        } catch (AuthenticationException ae) {
            log.warn("登录出错");
            throw ae;
        }
    }

    @GetMapping("test")
    public String test(){
        return "hello world";
    }
}
