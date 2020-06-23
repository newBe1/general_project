package com.example.service;

import com.example.entity.SysMenu;
import com.example.entity.SysRole;
import java.util.List;
import java.util.Set;

/**
 * 角色信息表(SysRole)表服务接口
 *
 * @author makejava
 * @since 2020-06-23 13:36:41
 */
public interface SysRoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param roleId 主键
     * @return 实例对象
     */
    SysRole queryById(Long roleId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<SysRole> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    SysRole insert(SysRole sysRole);

    /**
     * 修改数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    SysRole update(SysRole sysRole);

    /**
     * 通过主键删除数据
     *
     * @param roleId 主键
     * @return 是否成功
     */
    boolean deleteById(Long roleId);

    /**
     * 通过角色id 查询该角色所有的权限
     * @param userId
     * @return
     */
    Set<SysMenu> getMenusByUserId(Long userId);

}