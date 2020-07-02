package com.example.shiro.jwt;

import cn.hutool.core.util.StrUtil;
import com.example.entity.SysMenu;
import com.example.entity.SysRole;
import com.example.entity.SysUser;
import com.example.redis.RedisConstant;
import com.example.service.SysRoleService;
import com.example.service.SysUserService;
import com.example.uitls.RedisUtil;
import com.example.uitls.JwtUtil;
import com.example.uitls.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description: 自定义Realm，实现授权与认证
 * User: Ryan
 * Date: 2020-04-23
 * Time: 10:54
 */
@Component
@Slf4j
public class JwtRealm extends AuthorizingRealm {
    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;


    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 用户授权 获取权限信息,以便框架判断资源是否拥有权限
     *
     * @param
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("------执行授权--------");
        SysUser user = ShiroUtils.getSysUser();

        //获取用户成功 对此用户进行授权
        if (user != null) {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

            // 角色与权限字符串集合
            Collection<String> rolesCollection = new HashSet<>();
            Collection<String> premissionCollection = new HashSet<>();

            System.out.println("获取角色信息：" + user.getRoles());
            System.out.println("获取权限信息：" + user.getMenus());

            for (SysRole role : user.getRoles()) {
                rolesCollection.add(role.getRoleName());
            }

            for (SysMenu sysMenu : user.getMenus()) {
                rolesCollection.add(sysMenu.getMenuName());
            }

            //将角色和权限注入shiro中，由其来管理判断用户的角色和权限
            simpleAuthorizationInfo.addRoles(rolesCollection);
            simpleAuthorizationInfo.addStringPermissions(premissionCollection);
        }
        throw new UnknownAccountException("用户不存在");
    }

    /**
     * 用户认证
     *
     * @param
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("------执行认证--------");

        //获取token 中的用户名信息
        String jwt = (String) token.getCredentials();
        String userName = JwtUtil.getClaim(jwt, RedisConstant.USERNAME);

        if (StrUtil.isBlank(userName)) {
            throw new AuthenticationException("Token中用户名为空(The userName in Token is empty)");
        }

        SysUser model = new SysUser();
        model.setUserName(userName);
        SysUser user = sysUserService.queryByUserName(userName);
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }

        //开始认证
        if (JwtUtil.verify(jwt) && RedisUtil.hasKey(RedisConstant.PREFIX_SHIRO_ACCESS_TOKEN + userName)) {

            //获取redis中设置的token时间戳并与token中携带的时间戳进行比较
            String currentTimeMillsRedis = RedisUtil.get(RedisConstant.PREFIX_SHIRO_ACCESS_TOKEN + userName).toString();
            if (JwtUtil.getClaim(jwt, RedisConstant.CURRENT_TIME_MILLIS).equals(currentTimeMillsRedis)) {

                // 查询用户的角色和权限存到SimpleAuthenticationInfo中，这样在其它地方通过SecurityUtils.getSubject().getPrincipal()就能拿出用户的所有信息，包括角色和权限
                Set<SysRole> roles = sysUserService.getRolesByUserId(user.getUserId());
                user.setRoles(roles);

                Set<SysMenu> menus = sysRoleService.getMenusByUserId(user.getUserId());
                user.setMenus(menus);

                log.info("用户" + user.getUserName() + "正在使用token登录");

                //这里返回的是类似账号密码的东西，但是jwtToken都是jwt字符串。还需要一个该Realm的类名
                return new SimpleAuthenticationInfo(jwt, jwt, "JwtRealm");
            }
            throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
        }
        throw new AuthenticationException("token 无效请重新登陆");
    }

    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}