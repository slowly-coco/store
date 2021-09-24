package com.bjpowernode.controller;

import com.bjpowernode.pojo.Admin;
import com.bjpowernode.service.AdminService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/admin")
public class AdminAction {
    //    在所有的界面层，一定会有业务逻辑层的对象
    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/login")
    public String login(String name,String pwd,HttpServletRequest request) {
        Admin admin = adminService.login(name,pwd);
        System.out.println(adminService.getClass().getName());
        if (admin != null) {
//                登陆成功
            request.setAttribute("admin",admin);
            return "main";

        } else {
            request.setAttribute("errmsg","用户名或密码不正确");
            return "login";
        }
    }
}

