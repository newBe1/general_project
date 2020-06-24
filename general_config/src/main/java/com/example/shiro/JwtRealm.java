package com.example.shiro;

import com.example.entity.SysMenu;
import com.example.entity.SysRole;
import com.example.entity.SysUser;
import com.example.service.SysRoleService;
import com.example.service.SysUserService;
import com.example.uitls.JwtUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
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
public class JwtRealm extends AuthorizingRealm {
    private static Logger logger = Logger.getLogger(AuthorizingRealm.class);
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
        logger.info("------执行授权--------");
        Long userId = JwtUtil.getUserId(principals.toString());
        SysUser user = sysUserService.queryById(userId);

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
        logger.info("------执行认证--------");

        String jwt = (String) token.getCredentials();
        if (jwt == null) {
            throw new AuthenticationException("token过期，请重新登录");
        }

        //下面是验证这个user是否是真实存在的
        Long userId = JwtUtil.getUserId(jwt);

        if (userId == null || !JwtUtil.verify(jwt, userId)) {
            throw new AuthenticationException("token认证失败！");
        }

        SysUser user = sysUserService.queryById(userId);
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }

        // 查询用户的角色和权限存到SimpleAuthenticationInfo中，这样在其它地方
        // SecurityUtils.getSubject().getPrincipal()就能拿出用户的所有信息，包括角色和权限
        Set<SysRole> roles = sysUserService.getRolesByUserId(userId);
        user.setRoles(roles);

        Set<SysMenu> menus = sysRoleService.getMenusByUserId(userId);
        user.setMenus(menus);

        logger.info("用户" + user.getUserName() + "正在使用token登录");

        //这里返回的是类似账号密码的东西，但是jwtToken都是jwt字符串。还需要一个该Realm的类名
        return new SimpleAuthenticationInfo(jwt, jwt, "JwtRealm");
    }

    // 模拟Shiro用户加密，假设用户密码为123456
    public static void main(String[] args) {
        // 用户名
        String username = "yanrui";
        // 用户密码
        String password = "123456";
        // 加密方式
        String hashAlgorithName = "MD5";
        // 加密次数
        int hashIterations = 1024;
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        Object obj = new SimpleHash(hashAlgorithName, password,
                credentialsSalt, hashIterations);
        System.out.println(obj);
    }

    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
