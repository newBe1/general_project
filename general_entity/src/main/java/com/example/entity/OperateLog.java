package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-06-15
 * Time: 15:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperateLog {
    private Long id;

    private String username; //用户名

    private Integer operateType; //操作类型

    private String operateMsg; //操作说明

    private String methodName; //方法名

    private String params; //请求参数

    private String jsonResult; //返回参数

    private String ip; //ip地址

    private String operateUrl; //ip地址

    private Date createDate; //操作时间
}
