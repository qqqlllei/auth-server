package com.auth.server.security;

import com.auth.server.security.constants.SecurityConstant;
import com.auth.server.security.integration.AuthFailureHandler;
import com.auth.server.util.ApplicationContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by 李雷 on 2018/7/13.
 */
@Component
public class AuthAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    @Autowired
    private ClientDetailsService clientDetailsService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {


        String clientId = request.getParameter(SecurityConstant.REQUEST_CLIENT_ID);
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        Map<String, Object> additionalInformation = clientDetails.getAdditionalInformation();
        String authFailureHandlerBeanName =String.valueOf(additionalInformation.get(SecurityConstant.AUTH_FAILURE_HANDLER));
        AuthFailureHandler authenticationFailureHandler =  ApplicationContextHelper.getBean(authFailureHandlerBeanName,AuthFailureHandler.class);
        authenticationFailureHandler.onAuthenticationFailure(request,response,exception);
    }

}
