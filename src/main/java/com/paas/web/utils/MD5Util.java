package com.paas.web.utils;

/**
 * Created by yangyibo on 17/2/7.
 */

import org.springframework.util.DigestUtils;


/**
 * MD5加密工具
 */
public class MD5Util {

    public static String encode(String password) {
        return processEncode(password);
    }


    public static String processEncode(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }


    public static void main(String[] args) {
        System.out.println(MD5Util.encode("123456"));
        System.out.println(MD5Util.encode("admin"));
    }
}