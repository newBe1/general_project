package com.example.exception;

/**
 * Created with IntelliJ IDEA.
 * Description: 自定义401无权限异常(UnauthorizedException)
 * User: Ryan
 * Date: 2020-06-29
 * Time: 14:26
 */
public class CustomUnauthorizedException extends RuntimeException{
    public CustomUnauthorizedException(String msg){
        super(msg);
    }

    public CustomUnauthorizedException() {
        super();
    }
}
