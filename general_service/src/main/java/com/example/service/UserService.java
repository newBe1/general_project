package com.example.service;

import com.example.entity.Role;
import com.example.entity.User;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-04-23
 * Time: 10:12
 */
public interface UserService {
    User findByName(String username);

    User findUserById(String userId);

    Set<Role> getRolesByUserId(String userId);

    User selectUserByName(String username);

    // 查询用户信息、角色、权限
    User update(User user);

    // 封号
    User banUser(String username);

    // 获取所有用户
    List<User> getUserList();
}
