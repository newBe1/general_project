package com.example.service;

import com.example.entity.Permission;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-05-24
 * Time: 15:13
 */
public interface PermissionService {
    List<Permission> getAll();
}
