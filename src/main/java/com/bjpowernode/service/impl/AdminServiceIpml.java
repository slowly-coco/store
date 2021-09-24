package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.AdminMapper;
import com.bjpowernode.pojo.Admin;
import com.bjpowernode.pojo.AdminExample;
import com.bjpowernode.service.AdminService;
import com.bjpowernode.utils.MD5Util;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceIpml implements AdminService {
    //    在业务逻辑层中，一定会有数据访问层对象
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {
        AdminExample example = new AdminExample();
        example.createCriteria().andANameEqualTo(name);
        List<Admin> list = adminMapper.selectByExample(example);
        if (list.size() > 0) {
            Admin admin = list.get(0);
            String miPwd = MD5Util.getMD5(pwd);
            if (miPwd.equals(admin.getaPass())) {
                return admin;
            }
        }
        return null;
    }
}
