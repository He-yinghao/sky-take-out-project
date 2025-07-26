package com.sky.exception;

/**
 * 业务异常
 */
public class BaseException extends RuntimeException {

    // 无参构造器
    public BaseException() {
    }

    // 有参构造器
    public BaseException(String msg) {
        super(msg);
    }

}
