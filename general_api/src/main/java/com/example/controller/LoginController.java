package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.example.annotations.SysLog;
import com.example.dao.SysUserDao;
import com.example.entity.SysUser;
import com.example.enums.CodeMsg;
import com.example.redis.RedisConstant;
import com.example.service.SysUserService;
import com.example.uitls.JedisUtil;
import com.example.uitls.JwtUtil;
import com.example.uitls.ShiroUtils;
import com.example.utils.MyResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

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
@PropertySource("classpath:config.properties")
public class LoginController {
    @Resource
    private SysUserService sysUserService;

    /**
     * RefreshToken过期时间
     */
    @Value("${refreshTokenExpireTime}")
    private String refreshTokenExpireTime;

    @PostMapping("/login")
    @SysLog()
    public MyResult login(String userName , String passWord , HttpServletResponse httpServletResponse) {
        log.info("username:{},password:{}", userName, passWord);
        if (StrUtil.isBlank(userName) && StrUtil.isBlank(passWord)) {
            return MyResult.error(CodeMsg.PARAMETER_ISNULL);
        }

        SysUser user = sysUserService.queryByUserName(userName);
        if (user == null) {
            return MyResult.error(CodeMsg.USER_NOT_EXSIST);
        }
        String passKey = new Md5Hash(userName + passWord + user.getSalt()).toHex();

        if (passKey.equals(user.getPassword())) {

            //清除可能存在的shiro权限信息缓存
            if (JedisUtil.exists(RedisConstant.PREFIX_SHIRO_ACCESS_TOKEN + userName)) {
                JedisUtil.delKey(RedisConstant.PREFIX_SHIRO_ACCESS_TOKEN + userName);
            }

            //设置时间戳存入redis
            String currentTimeMills = String.valueOf(System.currentTimeMillis());
            JedisUtil.setObject(RedisConstant.PREFIX_SHIRO_ACCESS_TOKEN + userName, currentTimeMills, Integer.parseInt(refreshTokenExpireTime));

            //创建jwt并放入请求中
            String jwt = JwtUtil.sign(userName, currentTimeMills);
            httpServletResponse.setHeader("Authorization", jwt);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
            return MyResult.customerRet(HttpStatus.OK.value(), "登陆成功", user);
        }
        return MyResult.error(CodeMsg.ACCOUNT_PASSWORD_ERROR);
    }
        /*try {
            //获得当前用户到登录对象，现在状态为未认证
            Subject subject = ShiroUtils.getSubject();

            //用户名密码令牌
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName, passWord);

            //将令牌传到shiro提供的login方法验证，需要自定义realm
            subject.login(usernamePasswordToken);

            //登陆成功后 从shiro中获取用户信息
            SysUser sysUser = (SysUser) subject.getPrincipal();

            String jwt = JwtUtil.createToken(sysUser.getUserName());
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
    }*/

    @RequestMapping(path = "/unauthorized/{message}")
    public MyResult unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        return MyResult.customerRet(401,message,null);
    }

    @GetMapping("test")
    public MyResult test(){
        return MyResult.success("hello world");
    }

}
