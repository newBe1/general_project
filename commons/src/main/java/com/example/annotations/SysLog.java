package com.example.annotations;

import com.example.enums.OperateType;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-06-16
 * Time: 16:01
 */
@Target({ElementType.METHOD , ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 模块
     * @return
     */
    String module() default "系统";

    /**
     * 操作类型
     * @return
     */
    OperateType operateType() default OperateType.OTHER;

    /**
     * 操作说明
     * @return
     */
    String operateMsg() default "无声明操作";

}
