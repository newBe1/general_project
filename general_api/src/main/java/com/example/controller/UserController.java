package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.example.annotations.SysLog;
import com.example.entity.SysRole;
import com.example.entity.SysUser;
import com.example.enums.CodeMsg;
import com.example.enums.OperateType;
import com.example.redis.RedisConstant;
import com.example.service.SysUserService;
import com.example.uitls.RedisUtil;
import com.example.uitls.JwtUtil;
import com.example.uitls.ShiroUtils;
import com.example.uitls.UserUtil;
import com.example.utils.MyResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
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
public class UserController {
    @Resource
    private SysUserService sysUserService;

    @Resource
    private UserUtil userUtil;


    /**
     * RefreshToken过期时间
     */
    @Value("${refreshTokenExpireTime}")
    private String refreshTokenExpireTime;

    @PostMapping("/login")
    @SysLog(operateMsg = "登陆",operateType = OperateType.OTHER)
    public MyResult login(String userName , String passWord , HttpServletResponse httpServletResponse) {
        log.info("username:{},password:{}", userName, passWord);
        if (StrUtil.isBlank(userName) && StrUtil.isBlank(passWord)) {
            return MyResult.error(CodeMsg.PARAMETER_ISNULL);
        }

        SysUser user = sysUserService.queryByUserName(userName);
        if (user == null) {
            return MyResult.error(CodeMsg.USER_NOT_EXSIST);
        }
        String passKey = new Md5Hash(userName + user.getSalt() + passWord).toHex();

        if (passKey.equals(user.getPassword())) {

            //设置时间戳存入redis
            String currentTimeMills = String.valueOf(System.currentTimeMillis());
            RedisUtil.set(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + userName, currentTimeMills, Integer.parseInt(refreshTokenExpireTime));

            //创建jwt并放入请求头中
            String jwt = JwtUtil.sign(userName, currentTimeMills);
            httpServletResponse.setHeader("Authorization", jwt);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");

            return MyResult.customerRet(HttpStatus.OK.value(), "登陆成功", null);
        }
        return MyResult.error(CodeMsg.ACCOUNT_PASSWORD_ERROR);
    }


    @RequestMapping(path = "/unauthorized/{message}")
    public MyResult unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        return MyResult.customerRet(401,message,null);
    }

    @GetMapping("test")
    @SysLog(operateMsg = "测试")
    public MyResult test(){
        SysUser user = new SysUser();
        user.setUserName("fasdfa");
        user.setPassword("fadfas");
        return MyResult.success(sysUserService.insert(user));
    }

    /**
     * 获取当前登陆用户的详情信息
     * @return
     */
    @GetMapping("userInfo")
    @SysLog(operateMsg = "获取当前登陆用户详情信息",operateType = OperateType.QUERY)
    public MyResult userInfo(){

        SysUser user = userUtil.getCurrentUser();
        if(user == null){
            return MyResult.error(CodeMsg.USER_NOT_EXSIST);
        }
        return MyResult.success(user);
    }

}
