package com.example.shiro;

import com.example.shiro.jwt.JwtFilter;
import com.example.service.SysMenuService;
import com.example.shiro.cache.CustomCacheManager;
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
import java.util.HashMap;
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
        defaultWebSecurityManager.setCacheManager(new CustomCacheManager());
        return defaultWebSecurityManager;
    }

    //设置拦截器  对访问的接口进行拦截和过滤
    @Bean
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

        /**
         * anon：匿名用户可访问
         * authc：认证用户可访问
         * user：使用rememberMe可访问
         * perms：对应权限可访问
         * role：对应角色权限可访问
         **/
        Map<String, String> filterRuleMap = new HashMap<>();
        //设置所有请求通过jwtFilter
        filterRuleMap.put("/**","jwt");
        //访问 /unauthorized/** 不通过JWTFilter
        filterRuleMap.put("/sys/unauthorized/**", "anon");
        filterRuleMap.put("/sys/login/", "anon");

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
