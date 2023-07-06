package com.cspiration.mooc.aspect;

import com.alibaba.fastjson.JSON;
import com.cspiration.mooc.entity.WebLog;
import com.cspiration.mooc.support.OperLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class WebAspect {

    private static Logger logger = LoggerFactory.getLogger(WebAspect.class);

    @Pointcut("@annotation(com.cspiration.mooc.support.OperLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public void log(ProceedingJoinPoint proceedingJoinPoint) {
        logger.info("============请求内容开始============");

        WebLog webLog = new WebLog();

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        OperLog operLog = method.getAnnotation(OperLog.class);
        if (operLog != null) {
            String value = operLog.message();
            logger.info("获取操作：" + value);
            webLog.setMessage(value);
        }

        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        logger.info("请求类名" + className);
        logger.info("请求方法名" + methodName);
        webLog.setMethod(className + "." + methodName);

        Object[] args = proceedingJoinPoint.getArgs();
        String params = JSON.toJSONString(args);
        logger.info("请求参数: " + params);
        webLog.setParams(params);

        webLog.setCreateTime(new Date());
        logger.info("请求时间: " + webLog.getCreateTime());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        logger.info("请求URL: " + request.getRequestURI().toString());
        logger.info("请求HTTP方式: " + request.getMethod());
        logger.info("请求IP: " + request.getRemoteAddr());
        webLog.setIp(request.getRemoteAddr());

        long start = System.currentTimeMillis();
        Long time = System.currentTimeMillis() - start;
        logger.info("调用时间: " + time);
        webLog.setTotalTime(time);

        logger.info("============请求内容结束============");
    }

}
