package com.auth.server.fegin;

import com.auth.server.security.vo.SysUserAuthentication;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 李雷 on 2018/7/10.
 */
@FeignClient("user-server")
@RequestMapping("/userApi")
public interface UserFegin {
    @RequestMapping(value="/findUserByUsername")
    SysUserAuthentication findUserByUsername(@RequestParam("name") String name);

    @RequestMapping(value="/findUserByPhoneNumber")
    SysUserAuthentication findUserByPhoneNumber(@RequestParam("phone") String phone);


}
