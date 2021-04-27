package com.c2501.component.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 记录请求参数，统计执行时间
 * 
 * @author Nbb
 *
 */
@Component
public class RequestDetailLogFilter extends OncePerRequestFilter implements Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestDetailLogFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long beginTime = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();

        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            if (params.hasMoreElements()) {
                sb.append(name).append("=").append(request.getParameter(name)).append("&");
            } else {
                sb.append(name).append("=").append(request.getParameter(name));
            }
        }

        LOGGER.info("请求方式[{}]，请求路径[{}]，请求参数[{}]", request.getMethod(), request.getRequestURI(), sb.toString());

        filterChain.doFilter(request, response);

        LOGGER.info("请求耗时{}ms", System.currentTimeMillis() - beginTime);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10000;
    }

}
