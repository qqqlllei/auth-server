package com.auth.server.fegin;

import com.auth.server.security.vo.SysUserAuthentication;

/**
 * Created by 李雷 on 2018/8/10.
 */
public interface LoginAbstractFegin {

    SysUserAuthentication findUserById(String id);

    SysUserAuthentication findUserByPhone(String phone);
}
