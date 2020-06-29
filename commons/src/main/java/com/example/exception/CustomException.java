package com.example.exception;

/**
 * Created with IntelliJ IDEA.
 * Description: 自定义异常
 * User: Ryan
 * Date: 2020-06-29
 * Time: 14:26
 */
public class CustomException extends RuntimeException{
    public CustomException(String msg){
        super(msg);
    }

    public CustomException() {
        super();
    }
}
