package cn.itcast.web.controller;


import cn.itcast.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    //注入request
    @Autowired
    protected HttpServletRequest request;

    //注入response
    @Autowired
    protected HttpServletResponse response;

    //注入session
    @Autowired
    protected HttpSession session;

    // 获取登陆用户的所属企业id
    public String getLoginCompanyId(){
        return getLoginUser().getCompanyId();
    }

    // 获取登陆用户的所属企业name
    public String getLoginCompanyName(){
        return getLoginUser().getCompanyName();
    }

    // 从session中获取登录用户对象
    protected User getLoginUser(){
        return (User)session.getAttribute("loginInfo");
    }
}
