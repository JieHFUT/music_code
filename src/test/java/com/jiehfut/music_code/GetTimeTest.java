package com.jiehfut.music_code;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTimeTest {
    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String data = sdf.format(new Date());
        System.out.println(data);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String data2 = sdf2.format(new Date());
        System.out.println(data2);

    }
}
