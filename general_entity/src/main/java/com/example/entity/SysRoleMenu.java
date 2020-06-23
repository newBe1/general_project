package com.example.entity;

import java.io.Serializable;

/**
 * 角色和菜单关联表(SysRoleMenu)实体类
 *
 * @author makejava
 * @since 2020-06-23 13:35:35
 */
public class SysRoleMenu implements Serializable {
    private static final long serialVersionUID = -16552964563379827L;
    /**
    * 角色ID
    */
    private Long roleId;
    /**
    * 菜单ID
    */
    private Long menuId;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

}