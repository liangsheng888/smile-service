package org.flightythought.smile.appserver.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flightythought.smile.appserver.bean.ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Copyright 2019 Flighty-Thought All rights reserved.
 *
 * 登录失败
 * @Author: LiLei
 * @ClassName CustomerAuthenticationFailureHandler
 * @CreateTime 2019/3/17 19:32
 * @Description: TODO
 */
@Component
public class CustomerAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.info("登陆失败");

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        ResponseBean responseBean = ResponseBean.error("登录失败", exception.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(responseBean));
    }
}
