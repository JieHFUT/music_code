package com.jiehfut.music_code.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    // 定义一个固定的盐值
    private static final String salt = "jieHFUT";
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }
    /**
     * 第一次加密 ：模拟前端自己加密，然后传到后端
     * 123456 => iH123456UT =>MD5=> 49a3958d9d25c0d0797e34019f1fd33b
     * @param inputPass
     * @return
     */
    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(1)+salt.charAt(3) + inputPass
                +salt.charAt(5) + salt.charAt(6);
        return md5(str);
    }
    /**
     * 第2次MD5加密
     * 49a3958d9d25c0d0797e34019f1fd33b => je49a3958d9d25c0d0797e34019f1fd33bUF =>MD5=> 853e3caa59ac49a02adf8da9ee9ee937
     * @param formPass 前端加密过的密码，传给后端进行第2次加密
     * @param salt 用户数据库当中的盐值
     * @return
     */
    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5)
                + salt.charAt(4);
        return md5(str);
    }

    /**
     * 上面两个函数合到一起进行调用
     * @param inputPass
     * @param saltDB
     * @return
     */
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }



    public static void main(String[] args) {
        System.out.println("对用户输入密码进行第1次加密："+inputPassToFormPass("123456"));
        System.out.println("对用户输入密码进行第2次加密："+formPassToDBPass(inputPassToFormPass("123456"),
                "jieHFUT"));
        System.out.println("对用户输入密码进行第2次加密："+inputPassToDbPass("123456", "jieHFUT"));
    }



}
