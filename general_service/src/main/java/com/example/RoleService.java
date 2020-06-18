package com.example;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-05-24
 * Time: 15:37
 */
public interface RoleService {
    Set<Permission> getPersByRoleId(String roleId);
}
