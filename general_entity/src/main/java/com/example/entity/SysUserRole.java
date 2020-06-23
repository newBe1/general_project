package com.example.entity;

import java.io.Serializable;

/**
 * 用户和角色关联表(SysUserRole)实体类
 *
 * @author makejava
 * @since 2020-06-23 13:31:13
 */
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = 138381943290871414L;
    /**
    * 用户ID
    */
    private Long userId;
    /**
    * 角色ID
    */
    private Long roleId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}