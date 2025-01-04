package com.jiehfut.music_code;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        // 此
        exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})

public class MusicCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicCodeApplication.class, args);
    }

    
}
