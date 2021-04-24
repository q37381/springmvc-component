package com.c2501.component.aspect;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.c2501.component.constant.Constant;
import com.common.model.CommonReturnVO;

/**
 * 专门用来包装controller返回对象，加上code msg data
 * 
 * @author Nbb
 *
 */
@Aspect
//@Component
@Deprecated
public class ResponseSimpleBodyAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseSimpleBodyAspect.class);

    @Pointcut("@annotation(com.c2501.component.annotation.ResponseSimpleBody)")
    public void responseSimpleBody() {

    }

    // @Around("responseSimpleBody()")
    public Object responseSimpleBody(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        // 这里抛异常 会当做controller抛的异常，后面的exceptionhandle会捕获到
        // 响应输出json格式
        try {
            printJson(result);
        } catch (Throwable e) {
            LOGGER.error("向响应流写出执行结果时出现了异常", e);
            throw e;
        }

        return null;
    }

    // @Around("responseSimpleBody()")
    public Object responseSimpleBodyD(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        // 这里抛异常 会当做controller抛的异常，后面的exceptionhandle会捕获到
        // 包装成统一返回格式
        // 这样如果返回类型和controller不一致会报类转换异常
        CommonReturnVO ret = new CommonReturnVO();
        ret.setCode(Constant.RESPONSE_SUCCESS_CODE);
        ret.setMsg(Constant.RESPONSE_SUCCESS_MSG);
        ret.setData(result);

        return ret;
    }

    private void printJson(Object result) throws IOException {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getResponse();

        PrintWriter writer = response.getWriter();
        String code = Constant.RESPONSE_SUCCESS_CODE;
        String msg = Constant.RESPONSE_SUCCESS_MSG;
        StringBuilder jsonStrBuilder = new StringBuilder();

        jsonStrBuilder.append("{ \"data\":").append(JSONObject.toJSONString(result)).append(",");
        jsonStrBuilder.append("\"code\": \"").append(code).append("\",");
        jsonStrBuilder.append("\"msg\": \"").append(msg).append("\"");
        jsonStrBuilder.append(" }");

        writer.print(jsonStrBuilder.toString());
        // writer.flush();
    }

}
