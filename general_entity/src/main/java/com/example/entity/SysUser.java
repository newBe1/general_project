package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户信息表(SysUser)实体类
 *
 * @author makejava
 * @since 2020-06-23 13:34:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser implements Serializable {
    private static final long serialVersionUID = 711117755264684278L;
    /**
    * 用户ID
    */
    private Long userId;
    /**
    * 部门ID
    */
    private Long deptId;
    /**
    * 用户昵称
    */
    private String userName;
    /**
    * 用户类型（00系统用户 01注册用户）
    */
    private String userType;
    /**
    * 用户邮箱
    */
    private String email;
    /**
    * 手机号码
    */
    private String phonenumber;
    /**
    * 用户性别（0男 1女 2未知）
    */
    private String sex;
    /**
    * 头像路径
    */
    private String avatar;
    /**
    * 密码
    */
    private String password;
    /**
    * 盐加密
    */
    private String salt;
    /**
    * 帐号状态（0正常 1停用）
    */
    private String status;
    /**
    * 删除标志（0代表存在 2代表删除）
    */
    private String delFlag;
    /**
    * 最后登陆IP
    */
    private String loginIp;
    /**
    * 最后登陆时间
    */
    private Date loginDate;
    /**
    * 创建者
    */
    private String createBy;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 更新者
    */
    private String updateBy;
    /**
    * 更新时间
    */
    private Date updateTime;
    /**
    * 备注
    */
    private String remark;

    private Set<SysRole> roles;          //用户所有角色值，用于shiro做角色权限的判断
    private Set<SysMenu> menus;    //用户所有权限值，用于shiro做资源权限的判断
    private String jwt;            //用户登陆成功后的token
}