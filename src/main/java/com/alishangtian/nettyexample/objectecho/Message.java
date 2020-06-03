package com.alishangtian.nettyexample.objectecho;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description Message
 * @Date 2020/6/3 下午8:50
 * @Author maoxiaobing
 **/
public class Message implements Serializable {
    private String message;
    private int code;
    private static final AtomicInteger nums = new AtomicInteger();

    public Message(String message) {
        this.code = nums.getAndIncrement();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
