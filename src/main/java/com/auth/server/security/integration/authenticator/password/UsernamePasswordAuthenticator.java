package com.auth.server.security.integration.authenticator.password;

import com.alibaba.fastjson.JSONObject;
import com.auth.server.fegin.UserFegin;
import com.auth.server.security.constants.SecurityConstant;
import com.auth.server.security.integration.IntegrationAuthentication;
import com.auth.server.security.integration.authenticator.IntegrationAuthenticator;
import com.auth.server.security.vo.AuthUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary
public class UsernamePasswordAuthenticator implements IntegrationAuthenticator {

    @Autowired
    private UserFegin userFegin;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthUser authenticate(IntegrationAuthentication integrationAuthentication) {
        String password = integrationAuthentication.getAuthParameter(SecurityConstant.AUTH_AUTHORIZED_GRANT_PASSWORD);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(SecurityConstant.AUTH_AUTHORIZED_GRANT_LOGIN_NAME, integrationAuthentication.getUsername());
        paramMap.put(SecurityConstant.AUTH_AUTHORIZED_GRANT_PASSWORD, password);
        Map<String,Object> loginUserResult = userFegin.queryLoginUser(paramMap);
        AuthUser sysUserAuthentication = null;
        if(SecurityConstant.AUTH_LOGIN_SUCCESS_STATUS.equals(loginUserResult.get(SecurityConstant.AUTH_LOGIN_CODE_NAME))){
            JSONObject user = (JSONObject) JSONObject.toJSON(loginUserResult.get("user"));
            sysUserAuthentication = JSONObject.parseObject(user.toJSONString(),AuthUser.class);
            sysUserAuthentication.setPwdStatus(String.valueOf(loginUserResult.get("resultCodePwd")));
        }


        if(sysUserAuthentication !=null){
            sysUserAuthentication.setPassword(passwordEncoder.encode(password));
        }
        return sysUserAuthentication;
    }

    @Override
    public boolean prepare(IntegrationAuthentication integrationAuthentication, Map<String,Object> additionalInformation, HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {

        return (SecurityConstant.AUTH_AUTHORIZED_GRANT_PASSWORD.equals(integrationAuthentication.getAuthType())
                || StringUtils.isEmpty(integrationAuthentication.getAuthType()));
    }

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication, HttpServletRequest request, HttpServletResponse response) {

    }
}
