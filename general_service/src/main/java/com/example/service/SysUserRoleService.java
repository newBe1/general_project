package com.example.service;

import com.example.entity.SysUserRole;
import java.util.List;

/**
 * 用户和角色关联表(SysUserRole)表服务接口
 *
 * @author makejava
 * @since 2020-06-23 13:33:35
 */
public interface SysUserRoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    SysUserRole queryById(Long userId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<SysUserRole> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param sysUserRole 实例对象
     * @return 实例对象
     */
    SysUserRole insert(SysUserRole sysUserRole);

    /**
     * 修改数据
     *
     * @param sysUserRole 实例对象
     * @return 实例对象
     */
    SysUserRole update(SysUserRole sysUserRole);

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    boolean deleteById(Long userId);

}