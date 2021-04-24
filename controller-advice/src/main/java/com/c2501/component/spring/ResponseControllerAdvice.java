package com.c2501.component.spring;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.c2501.component.annotation.ResponseSimpleBody;
import com.c2501.component.constant.Constant;
import com.common.model.CommonExceptionReturnVO;
import com.common.model.CommonReturnVO;

// 写出的时候有一个RequestResponseBodyAdviceChain，实现此接口，会加入，调用会到这里来
// 加入了这个 如果在aop 的时候调用了writer写出， 在这里走到returnValueHandle写出的时候由于这里有返回值不为空，所以会再写一次
@ControllerAdvice
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getMethodAnnotation(ResponseSimpleBody.class) != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        if (body instanceof CommonExceptionReturnVO) {
            return body;
        }

        CommonReturnVO vo = new CommonReturnVO();
        vo.setCode(Constant.RESPONSE_SUCCESS_CODE);
        vo.setMsg(Constant.RESPONSE_SUCCESS_MSG);
        vo.setData(body);
        return vo;
    }

}
