package com.paas.web.service.impl;

import com.paas.web.domain.SysUser;
import com.paas.web.repository.SysUserRepository;
import com.paas.web.service.ISysUserService;
import com.paas.web.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements ISysUserService {
    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public SysUser findByUsername(String username) {
//        return sysUserRepository.findByUsername(username);
        SysUser user = new SysUser();
        user.setId(1);
        user.setUsername("root");
        user.setPassword(MD5Util.encode("1234567"));
        return user;
    }
}
