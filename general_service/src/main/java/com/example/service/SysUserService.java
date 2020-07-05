package com.example.service;

import com.example.entity.SysRole;
import com.example.entity.SysUser;
import java.util.List;
import java.util.Set;

/**
 * 用户信息表(SysUser)表服务接口
 *
 * @author makejava
 * @since 2020-07-05 15:28:07
 */
public interface SysUserService {

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    SysUser queryById(Long userId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<SysUser> queryAllByLimit(int offset, int limit);
    
    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysUser 实例对象
     * @return 对象列表
     */
    List<SysUser> queryAll(SysUser sysUser);

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    SysUser insert(SysUser sysUser);

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    SysUser update(SysUser sysUser);

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    boolean deleteById(Long userId);

    /**
     * 通过userId 查询该用户的所有角色
     * @param userId
     * @return
     */
    Set<SysRole> getRolesByUserId(Long userId);

    SysUser queryByUserName(String userName);

}