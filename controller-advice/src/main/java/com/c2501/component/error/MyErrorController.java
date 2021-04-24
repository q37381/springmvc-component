package com.c2501.component.error;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.c2501.component.constant.ErrorMsgGenerator;

/*
 * 注解继承只是类或接口上
 * 继承方法会继承父类的方法上的注解 待验证
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class MyErrorController extends BasicErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyErrorController.class);

    private static final Map<String, Object> COMMON_MSG_MAP = ErrorMsgGenerator.getDefaultExceptionMap();

    public MyErrorController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(serverProperties.getError().isIncludeException()),
                serverProperties.getError());
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);

        Map<String, Object> body = getErrorAttributes(request, true);

        LOGGER.error("请求出现错误，错误码{},错误信息{}", status, body);

        return new ResponseEntity<>(COMMON_MSG_MAP, HttpStatus.OK);
    }

}
