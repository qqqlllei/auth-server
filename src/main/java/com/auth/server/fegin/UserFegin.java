package com.auth.server.fegin;

import com.auth.server.security.vo.SysUserAuthentication;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by 李雷 on 2018/7/10.
 */
@FeignClient(name = "user-server" ,url = "outer.inner.zhongjiaxin.com")
@RequestMapping("/riskUserLogin")
public interface UserFegin {

    @RequestMapping(value="/findUserByUsername")
    SysUserAuthentication findUserByUsername(@RequestParam("name") String name);

    @RequestMapping(value = "/queryLoginUser", method = RequestMethod.POST)
    SysUserAuthentication queryLoginUser(@RequestBody Map<String, Object> paramMap);

    @RequestMapping(value="/findUserByPhoneNumber")
    SysUserAuthentication findUserByPhoneNumber(@RequestParam("phone") String phone);


}
