package com.example.enums;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-06-16
 * Time: 14:34
 */
public enum CodeMsg {
    // 按照模块定义CodeMsg
    // 通用异常
    SUCCESS(200,"success"),
    SERVER_EXCEPTION(500100,"服务端异常"),
    PARAMETER_ISNULL(500101,"输入参数为空"),
    // 业务异常
    USER_NOT_EXSIST(500102,"用户不存在"),
    ONLINE_USER_OVER(500103,"在线用户数超出允许登录的最大用户限制。"),
    SESSION_NOT_EXSIST(500104,"不存在离线session数据"),
    NOT_FIND_DATA(500105,"查找不到对应数据"),
    ACCOUNT_PASSWORD_ERROR(500106,"账号密码错误"),
    NO_ACCESS(500107,"无访问权限");

    private Integer code;
    private String message;

    private CodeMsg(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
