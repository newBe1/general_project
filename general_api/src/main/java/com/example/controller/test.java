package com.example.controller;

import com.example.uitls.JedisUtil;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-06-30
 * Time: 11:43
 */
public class test {
    public static void main(String[] args) {
        String object = JedisUtil.getObject("shiro").toString();
        System.out.println(object);
    }
}
