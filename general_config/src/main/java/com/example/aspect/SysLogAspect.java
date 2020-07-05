package com.example.aspect;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.example.annotations.SysLog;
import com.example.entity.OperateLog;
import com.example.entity.SysUser;
import com.example.service.OperateLogService;
import com.example.uitls.ShiroUtils;
import com.example.utils.ServletUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:  日志切面类
 * User: Ryan
 * Date: 2020-06-15
 * Time: 14:49
 */
@Aspect
@Component
public class SysLogAspect {
    private static final Logger log = LoggerFactory.getLogger(SysLogAspect.class);
    @Resource
    private OperateLogService operateLogService;

    /**
     * 定义切点 @PointCut  在注解的位置切入代码
     */
    @Pointcut("@annotation(com.example.annotations.SysLog)")
    public void logPointCut(){

    }

    /**
     * @Aspect 表明是一个切面类
     * @Component 将当前类注入到Spring容器内
     * @Pointcut 切入点，其中execution用于使用切面的连接点。使用方法：execution(方法修饰符(可选) 返回类型 方法名 参数 异常模式(可选)) ，
     * 可以使用通配符匹配字符，*可以匹配任意字符。
     * @Before 在方法前执行
     * @After 在方法后执行
     * @AfterReturning 在方法执行后返回一个结果后执行
     * @AfterThrowing 在方法执行过程中抛出异常的时候执行
     * @Around 环绕通知，就是可以在执行前后都使用，这个方法参数必须为ProceedingJoinPoint，proceed()方法就是被切面的方法，
     * 上面四个方法可以使用JoinPoint，JoinPoint包含了类名，被切面的方法名，参数等信息。
     * @param joinPoint
     */


    /**
     * 处理完请求后执行
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult)
    {
        saveSysLog(joinPoint, null, jsonResult);
    }


    /**
     * 拦截异常操作
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()" , throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint , Exception e){
        saveSysLog(joinPoint , e , null);
    }


    public void saveSysLog(JoinPoint joinPoint , Exception e , Object jsonResult){
        try {
            //保存日志
            OperateLog operateLog = new OperateLog();

            //从切面织入点通过反射机制获取织入点
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取织入点的方法
            Method method = signature.getMethod();

            //获取请求的方法名
            String methodName = method.getName();


            //获取操作
            SysLog sysLog = method.getAnnotation(SysLog.class);
            if(sysLog != null){
                Integer operateType = sysLog.operateType().ordinal();     //获取该枚举对象的位置的索引值
                String operateMsg = sysLog.operateMsg();
                String moudle = sysLog.module();

                operateLog.setOperatorMsg(operateMsg);
                operateLog.setOperateType(operateType);
                operateLog.setModule(moudle);
            }

            //获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            operateLog.setMethodName(className + "." +methodName +"()");

            //获取请求参数
            Map<String, String[]> map = ServletUtils.getRequest().getParameterMap();
            String params = JSON.toJSONString(map);
            operateLog.setOperateParam(params);

            //获取返回参数
            operateLog.setJsonResult(JSON.toJSONString(jsonResult));

            //获取请求时间
            operateLog.setOperateTime(new Date());

            //获取操作用户
            SysUser user = ShiroUtils.getSysUser();
            if(user != null){
                operateLog.setOperateName(user.getUserName());
            }

            //获取操作用户的ip地址
            String ip = ShiroUtils.getIp();
            operateLog.setOperateIp(ip);

            //获取操作url
            operateLog.setOperateUrl(ServletUtils.getRequest().getRequestURI());

            //获取请求方法
            operateLog.setRequestMethod(ServletUtils.getRequest().getMethod());

            operateLog.setStatus(1);

            //异常信息记录
            if(e != null){
                operateLog.setErrorMsg(StrUtil.sub(e.getMessage(),0,200));
                operateLog.setStatus(0);
            }

            //保存日志到数据库
            operateLogService.insert(operateLog);
        } catch (Exception ex) {
            log.error("-------前置通知异常 ， 异常信息：{}--------" + ex.getMessage());
            ex.printStackTrace();
        }
    }


}
