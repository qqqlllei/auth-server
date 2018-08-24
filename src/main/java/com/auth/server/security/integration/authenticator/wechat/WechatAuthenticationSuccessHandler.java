package com.auth.server.security.integration.authenticator.wechat;


import com.alibaba.fastjson.JSONObject;
import com.auth.server.security.constants.SecurityConstant;
import com.auth.server.security.integration.AuthSuccessHandler;
import com.auth.server.security.vo.SysUserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by 李雷 on 2018/8/8.
 */
@Component
public class WechatAuthenticationSuccessHandler implements AuthSuccessHandler {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication,OAuth2AccessToken token,OAuth2Authentication oAuth2Authentication,
                                        SysUserAuthentication sysUserAuthentication,ClientDetails clientDetails) throws ServletException, IOException {
        Map<String,Object> tokenAdditionalInformation = token.getAdditionalInformation();
        String sessionKey = clientDetails.getClientId()+"_"+sysUserAuthentication.getOpenId();
        tokenAdditionalInformation.put(SecurityConstant.AUTH_SESSION_KEY,sessionKey);
        tokenAdditionalInformation.put(SecurityConstant.WECHAT_OPENID_PARAM_NAME,sysUserAuthentication.getOpenId());
        String tokenValue =token.getValue();
        token = jwtAccessTokenConverter.enhance(token,oAuth2Authentication);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(sysUserAuthentication);
        jsonObject.put(SecurityConstant.TOKEN_VALUE,tokenValue);
        stringRedisTemplate.opsForValue().set(sessionKey,jsonObject.toJSONString(),clientDetails.getAccessTokenValiditySeconds(), TimeUnit.SECONDS);
        response.setContentType("application/json;charset=UTF-8");
        Map<String,String> result = new HashMap<>();
        result.put("resultCode",sysUserAuthentication.getStatus());
        result.put("appToken",token.getValue());
        response.getWriter().write(JSONObject.toJSONString(result));
    }
}
