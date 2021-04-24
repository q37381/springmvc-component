package com.c2501.component.aspect;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.c2501.component.annotation.ResponseSimpleBody;
import com.c2501.component.constant.Constant;
import com.common.constant.SomeEnums.DASErrorCode;
import com.common.exce.DASRuntimeException;
import com.google.common.collect.Lists;

/**
 * controller执行切面
 * 
 * @author Nbb
 *
 */
// 目前只能实现拦截注解，或拦截整个controller，不能实现拦截controller又拦截注解
// 报错返回code msg 是否也在这一层做了，这样的话 要指定拦截的controller 不如注解或者handlerExceptionResolver专门用来做这个的？
@Aspect
//@Component
@Deprecated
public class ControllerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("@annotation(com.c2501.component.annotation.ResponseSimpleBody)")
    public void responseSimpleBody() {

    }

    @Around("responseSimpleBody()")
    public Object responseSimpleBody(ProceedingJoinPoint point) {
        long beginTime = System.currentTimeMillis();

        Throwable ex = null;
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable e) {
            ex = e;
        }

        // 响应输出json格式
        try {
            printJson(ex, result);
        } catch (Throwable e) {
            LOGGER.error("向响应流写出执行结果时出现了异常", e);
        }

        // 调试模式会打印传入参数
        // 使用数组可能效率更高
        // 记录日志打印参数
        List<Object> logArgs = Lists.newArrayList();

        String controllerName = point.getTarget().getClass().getName();
        logArgs.add(controllerName);

        MethodSignature s = (MethodSignature) point.getSignature();
        s.getMethod().getAnnotation(ResponseSimpleBody.class);
        String methodName = s.getName();
        logArgs.add(methodName);

        String[] parameterNames = s.getParameterNames();
        Object[] args = point.getArgs();

        StringBuilder logPattern = new StringBuilder("{}.{}(");
        for (int i = 0; i < parameterNames.length; i++) {
            if (LOGGER.isDebugEnabled()) {
                if (i == 0) {
                    logPattern.append("{} {}:{}");
                } else {
                    logPattern.append(", {} {}:{}");
                }

                logArgs.add(s.getParameterTypes()[i].getCanonicalName());
                logArgs.add(parameterNames[i]);
                logArgs.add(args[i]);
            }
        }

        logPattern.append(") 执行耗时{}ms");
        logArgs.add(System.currentTimeMillis() - beginTime);

        if (ex != null) {
            logPattern.append("，出现了一个错误如下");
            logArgs.add(ex);
            LOGGER.error(logPattern.toString(), logArgs.toArray());
        } else {
            LOGGER.info(logPattern.toString(), logArgs.toArray());
        }

        return null;
    }

    private void printJson(Throwable ex, Object result) throws IOException {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getResponse();

        // TODOM :后期可以尝试只包装对象，由框架序列化响应回球
        response.setStatus(200);
        PrintWriter writer = response.getWriter();
        String code = Constant.RESPONSE_SUCCESS_CODE;
        String msg = Constant.RESPONSE_SUCCESS_MSG;
        StringBuilder jsonStrBuilder = new StringBuilder();
        if (ex != null) {
            // code msg 不同项目之间调用不是直接传递枚举
            code = DASErrorCode.UNKNOWN_ERROR.getCode();
            msg = DASErrorCode.UNKNOWN_ERROR.getDesc();
            if (ex instanceof DASRuntimeException) {
                code = ((DASRuntimeException) ex).getCode();
                msg = ((DASRuntimeException) ex).getDesc();
            }
            jsonStrBuilder.append("{ ");
        } else {
            jsonStrBuilder.append("{ \"data\":").append(JSONObject.toJSONString(result)).append(",");
        }
        jsonStrBuilder.append("\"code\": \"").append(code).append("\",");
        jsonStrBuilder.append("\"msg\": \"").append(msg).append("\"");
        jsonStrBuilder.append(" }");

        writer.print(jsonStrBuilder.toString());
        // writer.flush();
    }

}
