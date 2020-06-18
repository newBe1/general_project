package com.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description: 用户实体类
 * User: Ryan
 * Date: 2020-04-23
 * Time: 10:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String password;
    private Set<Role> roles = new HashSet<>();          //用户所有角色值，用于shiro做角色权限的判断
    private Set<Permission> perms = new HashSet<>();    //用户所有权限值，用于shiro做资源权限的判断
}
