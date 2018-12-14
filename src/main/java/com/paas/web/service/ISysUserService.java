package com.paas.web.service;

import com.paas.web.domain.SysUser;

public interface ISysUserService {
    SysUser findByUsername(String username);
}
