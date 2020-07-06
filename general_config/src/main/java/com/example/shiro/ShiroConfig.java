package com.example.shiro;

import com.example.shiro.jwt.JwtFilter;
import com.example.service.SysMenuService;
import com.example.shiro.jwt.JwtRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description: shiro配置类
 * User: Ryan
 * Date: 2020-04-23
 * Time: 11:22
 */
@Configuration
public class ShiroConfig {
    @Resource
    private SysMenuService sysMenuService;

    /**
     * 配置使用自定义Realm，关闭Shiro自带的session
     * 详情见文档 http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
     * @param jwtRealm
     * @return org.apache.shiro.web.mgt.DefaultWebSecurityManager
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean("securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(JwtRealm jwtRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 使用自定义Realm
        defaultWebSecurityManager.setRealm(jwtRealm);
        // 关闭Shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        // 设置自定义Cache缓存
        //defaultWebSecurityManager.setCacheManager(new CustomCacheManager());
        return defaultWebSecurityManager;
    }

    //设置拦截器  对访问的接口进行拦截和过滤
    /**
     * 添加自己的过滤器，自定义url规则
     * Shiro自带拦截器配置规则
     * rest：比如/admins/user/**=rest[user],根据请求的方法，相当于/admins/user/**=perms[user：method] ,其中method为post，get，delete等
     * port：比如/admins/user/**=port[8081],当请求的url的端口不是8081是跳转到schemal：//serverName：8081?queryString,其中schmal是协议http或https等，serverName是你访问的host,8081是url配置里port的端口，queryString是你访问的url里的？后面的参数
     * perms：比如/admins/user/**=perms[user：add：*],perms参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，比如/admins/user/**=perms["user：add：*,user：modify：*"]，当有多个参数时必须每个参数都通过才通过，想当于isPermitedAll()方法
     * roles：比如/admins/user/**=roles[admin],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，当有多个参数时，比如/admins/user/**=roles["admin,guest"],每个参数通过才算通过，相当于hasAllRoles()方法。//要实现or的效果看http://zgzty.blog.163.com/blog/static/83831226201302983358670/
     * anon：比如/admins/**=anon 没有参数，表示可以匿名使用
     * authc：比如/admins/user/**=authc表示需要认证才能使用，没有参数
     * authcBasic：比如/admins/user/**=authcBasic没有参数表示httpBasic认证
     * ssl：比如/admins/user/**=ssl没有参数，表示安全的url请求，协议为https
     * user：比如/admins/user/**=user没有参数表示必须存在用户，当登入操作时不做检查
     * 详情见文档 http://shiro.apache.org/web.html#urls-
     **/
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        //添加自定义的过滤器取名为jwt
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwt", new JwtFilter());
        factoryBean.setFilters(filterMap);

        // 设置 SecurityManager
        factoryBean.setSecurityManager(securityManager);

        // 设置登录成功跳转Url
        factoryBean.setSuccessUrl("/main");
        // 设置登录跳转Url
        factoryBean.setLoginUrl("/sys/login");
        // 设置未授权提示Url
        factoryBean.setUnauthorizedUrl("/sys/unauthorized/无权限");

        // 自定义url规则使用LinkedHashMap有序Map
        LinkedHashMap<String, String> filterRuleMap = new LinkedHashMap<String,String>(16);

        //访问 /unauthorized/** 和登陆 不通过JWTFilter
        filterRuleMap.put("/sys/unauthorized/**", "anon");
        filterRuleMap.put("/sys/login/**", "anon");
        //filterRuleMap.put("/sys/test/**", "anon");

        //设置所有请求通过jwtFilter
        filterRuleMap.put("/**","jwt");

        //只要在Permission表的资源都要做权限拦截  以permission.getUrl() 作为拦截路径
        /*List<SysMenu> sysMenuList = sysMenuService.queryAll(null);
        for (SysMenu sysMenu : sysMenuList) {
            //reslt.put("/authorized.html", "perms[user:add]");//由于是代码，可以注入Service从数据库中查询
            filterRuleMap.put(sysMenu.getUrl(), "crmPerms[" + sysMenu.getPerms() + "]");
        }*/

        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /**
     * 添加注解支持
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
