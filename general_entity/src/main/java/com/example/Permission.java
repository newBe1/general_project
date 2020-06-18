package com.example;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description: 权限类
 * User: Ryan
 * Date: 2020-04-23
 * Time: 10:04
 */
@Data
@AllArgsConstructor
public class Permission {
    private String id;
    private String name;
    private String url;
    private String perm;
}
