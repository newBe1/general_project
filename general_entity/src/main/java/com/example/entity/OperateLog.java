package com.example.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 操作日志记录(OperateLog)实体类
 *
 * @author makejava
 * @since 2020-07-05 15:44:52
 */
public class OperateLog implements Serializable {
    private static final long serialVersionUID = -49599912182039622L;
    /**
    * 日志主键
    */
    private Long operateId;
    /**
    * 模块标题
    */
    private String module;
    /**
    * 业务类型
    */
    private Integer operateType;
    /**
    * 方法名称
    */
    private String methodName;
    /**
    * 请求方式
    */
    private String requestMethod;
    /**
    * 操作说明
    */
    private String operatorMsg;
    /**
    * 操作人员
    */
    private String operateName;
    /**
    * 请求URL
    */
    private String operateUrl;
    /**
    * 主机地址
    */
    private String operateIp;
    /**
    * 请求参数
    */
    private String operateParam;
    /**
    * 返回参数
    */
    private String jsonResult;
    /**
    * 操作状态（0正常 1异常）
    */
    private Integer status;
    /**
    * 错误消息
    */
    private String errorMsg;
    /**
    * 操作时间
    */
    private Date operateTime;


    public Long getOperateId() {
        return operateId;
    }

    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getOperatorMsg() {
        return operatorMsg;
    }

    public void setOperatorMsg(String operatorMsg) {
        this.operatorMsg = operatorMsg;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getOperateUrl() {
        return operateUrl;
    }

    public void setOperateUrl(String operateUrl) {
        this.operateUrl = operateUrl;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }

    public String getOperateParam() {
        return operateParam;
    }

    public void setOperateParam(String operateParam) {
        this.operateParam = operateParam;
    }

    public String getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

}