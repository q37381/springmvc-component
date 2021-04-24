package com.c2501.component.spring;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.common.constant.SomeEnums.DASErrorCode;
import com.common.exce.DASRuntimeException;

//@Component
@Deprecated
// aop切面环绕处理异常后，到不了这里
/*
 * 返回null的话 抛了异常会到basicErrorController 错误页面 tomcat
 * catalina里面有一个额errorPage会转发到/error（启动的时候设置进去的，onRefresh()->createWebServer()
 * tomcat的factory 设置错误页面） 就到了basicErrorController 需要返回 new ModelAndView
 * 
 * 网传推荐使用@ExceptionHandle
 */
public class ExceptionHandler implements HandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {

        LOGGER.error("ajax请求出现异常", ex);

        String code = DASErrorCode.UNKNOWN_ERROR.getCode();
        String desc = DASErrorCode.UNKNOWN_ERROR.getDesc();

        StringBuilder sb = new StringBuilder();
        if (ex instanceof DASRuntimeException) {
            DASRuntimeException dasEx = (DASRuntimeException) ex;
            code = dasEx.getCode();
            desc = dasEx.getDesc();
        }

        sb.append("{");
        sb.append("\"code\": \"" + code + "\",");
        sb.append("\"msg\": \"" + desc + "\"");
        sb.append("}");

        response.setContentType("application/json;charset=UTF-8");

        try {
            PrintWriter pw = response.getWriter();
            pw.print(sb.toString());
            pw.flush();
        } catch (IOException e) {
            LOGGER.info("输出流异常", e);
        }

        return null;
    }

}
