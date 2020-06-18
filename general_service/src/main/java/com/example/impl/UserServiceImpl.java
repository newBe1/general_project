package com.example.impl;

import com.example.UserMapper;
import com.example.Role;
import com.example.User;
import com.example.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-04-23
 * Time: 13:04
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User findByName(String name) {
        // 查询用户是否存在
        User bean = userMapper.findByName(name);
        if (bean != null) {
            // 查询用户信息、角色、权限
            bean = userMapper.findById(bean.getId());
        }
        return null;
    }

    @Override
    public User findUserById(String userId) {
        return userMapper.findById(userId);
    }

    @Override
    public Set<Role> getRolesByUserId(String userId) {
        return null;
    }

    @Override
    public User selectUserByName(String username) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User banUser(String username) {
        return null;
    }

    @Override
    public List<User> getUserList() {
        return null;
    }
}
