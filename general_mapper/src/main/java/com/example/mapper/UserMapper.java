package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-04-23
 * Time: 11:28
 */
@Mapper
public interface UserMapper {
    // 查询用户信息
    User findByName(String username);

    // 查询用户信息、角色、权限
    User findById(String id);

    // 查询用户信息、角色、权限
    User update(User user);

    // 封号
    User banUser(String username);

    // 获取所有用户
    List<User> getUserList();
}
