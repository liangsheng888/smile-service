package org.flightythought.smile.appserver.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.flightythought.smile.appserver.bean.ResponseBean;
import org.flightythought.smile.appserver.bean.SmsCodeData;
import org.flightythought.smile.appserver.common.LocalCache;
import org.flightythought.smile.appserver.common.utils.AesUtils;
import org.flightythought.smile.appserver.common.utils.HttpUtils;
import org.flightythought.smile.appserver.common.utils.PhoneUtil;
import org.flightythought.smile.appserver.config.properties.AppProperties;
import org.flightythought.smile.appserver.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2019 Flighty-Thought All rights reserved.
 *
 * @Author: LiLei
 * @ClassName LoginServiceImpl
 * @CreateTime 2019/3/28 23:24
 * @Description: 登录处理业务层
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final String APP_CODE = "4e84bebc30684ce681c98547087784db";

    private static final Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    private AppProperties appProperties;

    @Override
    public ResponseBean getSMSVerificationCode(String phone, HttpSession session) {
        // 创建短信验证码加密对象
        SmsCodeData smsCodeData = new SmsCodeData();
        LOG.info("请求发送验证码的手机号为：{}", phone);

        if (StringUtils.isBlank(phone)) {
            return ResponseBean.error("请输入正确的手机号");
        }

        if (!PhoneUtil.isMobileNO(phone)) {
            return ResponseBean.error("请输入正确的手机号");
        }

        if (LocalCache.hasRegisterRequest(phone)) {
            return ResponseBean.error("一分钟只能只能请求一次短信验证码，请一分钟之后再次尝试");
        }

        // 创建4位数验证码
        int code = (int) Math.ceil(Math.random() * 9000 + 1000);
//        Map<String, Object> map = new HashMap<>(2);
//        map.put("mobile", phone);
//        map.put("code", code);

        String host = "http://yzxyzm.market.alicloudapi.com";
        String path = "/yzx/verifySms";
        String method = "POST";
        Map<String, String> headers = new HashMap<>();
        // 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
        headers.put("Authorization", "APPCODE " + APP_CODE);
        Map<String, String> queries = new HashMap<>();
        queries.put("phone", phone);
        queries.put("templateId", "TP18040314");
        queries.put("variable", "code:" + code);
        Map<String, String> bodies = new HashMap<>();
        try {
            /*
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, queries, bodies);
            LOG.info(response.toString());
        } catch (Exception e) {
            LOG.error("发送短信验证码失败", e);
            return ResponseBean.error("发送短信验证码失败", e.getMessage());
        }
        smsCodeData.setPhone(phone);
        smsCodeData.setVCode(code + "");
        smsCodeData.setTime(System.currentTimeMillis());

//        session.setAttribute("smsCode", map);

        try {
            String codeResult = AesUtils.aesEncryptHexString(JSON.toJSONString(smsCodeData), appProperties.getTokenKey());
            return ResponseBean.ok("发送短信成功", codeResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBean.error("发送短信失败", e.getMessage());
        }
    }
}
