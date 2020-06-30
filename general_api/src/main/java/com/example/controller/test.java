package com.example.controller;

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
        String s = new Md5Hash("admin" +"12345" + "111111").toHex();
        System.out.println(s);
    }
}
