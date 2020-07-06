package com.example.uitls;

import com.example.entity.SysMenu;
import com.example.entity.SysRole;
import com.example.entity.SysUser;
import com.example.exception.CustomException;
import com.example.redis.RedisConstant;
import com.example.service.SysRoleService;
import com.example.service.SysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description: 系统用户工具类
 * User: Ryan
 * Date: 2020-07-06
 * Time: 11:02
 */
@Component
public class UserUtil {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;

    /**
     * 获取系统当前用户的详细信息
     * @return
     */
    public SysUser getCurrentUser(){

        String jwt = ShiroUtils.getSubject().getPrincipal().toString();
        String userName = JwtUtil.getClaim(jwt, RedisConstant.USERNAME);

        SysUser user = sysUserService.queryByUserName(userName);
        if (user == null) {
            throw new CustomException("用户不存在");
        }
        Set<SysRole> roles = sysUserService.getRolesByUserId(user.getUserId());
        user.setRoles(roles);

        Set<SysMenu> menus = sysRoleService.getMenusByUserId(user.getUserId());
        user.setMenus(menus);
        user.setJwt(jwt);
        return user;
    }
}
