package com.jiehfut.music_code.utils;


import lombok.Data;

@Data
public class ResponseBodyMessage<T> {
    private int status;//状态码
    private String message;//状态描述信息
    private T data;//返回的数据

    public ResponseBodyMessage(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
