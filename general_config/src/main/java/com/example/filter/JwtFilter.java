package com.example.filter;

import com.example.shiro.JwtToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.log4j.Logger;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:  shiro 控制访问拦截器 处理是否允许访问/当访问被拒绝时如何处理等
 * User: Ryan
 * Date: 2020-04-23
 * Time: 14:09
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {
    private static Logger logger = Logger.getLogger(JwtFilter.class);
    /*
     * 1. 返回true，shiro就直接允许访问url
     * 2. 返回false，shiro才会根据onAccessDenied的方法的返回值决定是否允许访问url
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        logger.warn("isAccessAllowed 方法被调用 判断请求头是否带有token ");
        if(((HttpServletRequest) request).getHeader("Token") != null){
            //如何存在 则进入executeLogin 方法执行登入，检查token 是否有效
            try {
                executeLogin(request,response);
                return true;
            }catch (Exception e){
                //token错误
                return false;
            }

        }

        //如果请求头中不存在token 则可能是执行登陆操作，无需检查token 放回true
        return true;
    }

    /**
     * 返回结果为true表明登录通过
     */
    @Override
    protected boolean executeLogin(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        logger.warn("executeLogin 方法被调用  检验token 是否有效");

        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String token = httpServletRequest.getHeader("Token");
        JwtToken jwtToken = new JwtToken(token);

        //提交给reaml进行登入 如果错误会抛出异常进行捕获
        getSubject(servletRequest , servletResponse);

        //执行方法中没有抛出异常就表示登录成功
        return true;
    }

    private void responseError(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("login error");
    }
}
