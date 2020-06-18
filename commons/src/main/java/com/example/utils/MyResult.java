package com.example.utils;

import com.example.enums.CodeMsg;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:  返回值封装类
 * User: Ryan
 * Date: 2020-05-25
 * Time: 8:45
 */
@Data
public class MyResult {
    private String message;
    private Integer code;
    private Object data;


    public MyResult(Object data) {
        this.code = CodeMsg.SUCCESS.getCode();
        this.message = CodeMsg.SUCCESS.getMessage();
        this.data = data;
    }

    public MyResult(String message) {
        this.code = CodeMsg.SUCCESS.getCode();
        this.message = message;
    }


    public MyResult(CodeMsg cm){
        if(cm == null){
            return;
        }
        this.code = cm.getCode();
        this.message = cm.getMessage();

    }

    public MyResult(Integer code, String message, Object data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    /**
     * 成功 返回data
     * @param data
     * @param
     * @return
     */
    public static MyResult success(Object data){
        return new MyResult(data);
    }

    /**
     * 成功 不返回 自定义message
     * @param message
     * @return
     */
    public static MyResult success(String message){
        return new MyResult(message);
    }



    /**
     * 失败 返回定义好的CodeMsg
     * @param cm
     * @param
     * @return
     */
    public static MyResult error(CodeMsg cm){
        return new MyResult(cm);
    }


    /**
     * 自定义的返回方法
     * @param message
     * @param code
     * @param data
     * @param
     * @return
     */
    public static MyResult customerRet(Integer code , String message , Object data){
        return new MyResult(code,  message,  data);
    }
}
