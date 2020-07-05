package com.example.shiro.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-05-24
 * Time: 15:11
 */
public class JwtToken implements AuthenticationToken {
    private String jwt;

    public JwtToken(String jwt) {
        this.jwt = jwt;
    }

    //类似是用户名
    @Override
    public Object getPrincipal() {
        return jwt;
    }

    //类似密码
    @Override
    public Object getCredentials() {
        return jwt;
    }
}
