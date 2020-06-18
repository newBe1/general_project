package com.example.impl;

import com.example.Permission;
import com.example.RoleService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-05-24
 * Time: 15:37
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public Set<Permission> getPersByRoleId(String roleId) {
        return null;
    }
}
