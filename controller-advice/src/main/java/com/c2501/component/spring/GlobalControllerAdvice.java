package com.c2501.component.spring;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.c2501.component.constant.ErrorMsgGenerator;
import com.common.model.CommonExceptionReturnVO;

@ControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(Throwable.class)
    // 需要加上此注解，不然会当成视图，选择处理视图返回值的处理器来处理，导致没有视图所以使用默认当前视图导致循环报错
    // 加上注解会 mavContainer.setRequestHandled(true); 所以返回不会再由框架加上视图
    @ResponseBody
    public CommonExceptionReturnVO exception(HttpServletRequest request, Throwable e) {
        LOGGER.error("ajax请求出现异常", e);
        return ErrorMsgGenerator.getExceptionCommonReturnVO(e);
    }
}
