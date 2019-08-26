package cn.itcast.web.controller;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LoginController extends BaseController{


    @Autowired
    private ModuleService moduleService;

    /**
       shiro完成登录认证
     */
    @RequestMapping("/login")
    public String login(String email,String password){
        // A.判断参数:如果邮箱(用户)，密码为空
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            //跳转到登录页面
            return "forward:/login.jsp";
        }

        try{
            //B.登录认证
            //B1.获取/创建subject(第一次是创建)
         Subject subject =SecurityUtils.getSubject();
           // B2.创建token，封装输入的账户和密码
            AuthenticationToken token = new UsernamePasswordToken(email,password);
            //B3 登录认证
            subject.login(token);

            //B4.获取用户认证的身份对象(realm的认证方法构造器的第一个参数)
            User user = (User) subject.getPrincipal();

            //C.登录成功，保存：登录用户、用户权限
            session.setAttribute("loginInfo",user);
            // 调用service方法，通过用户id 查找用户的权限，并保存在会话域
            List<Module> modules = moduleService.findModuleByUserId(user.getId());
            session.setAttribute("modules",modules);

            return "home/main";

        }catch (Exception e){
            e.printStackTrace();
            request.setAttribute("error","登录认证失败！");
            return "forward:/login.jsp";
        }

    }

    /**
     * 执行流程详解
     *  main.jsp 中内嵌了iframe，src地址是"home.do"
     */
    @RequestMapping("/home")
    public String home(){
        return "home/home";
    }


    /**
     * 注销/退出
     */
    @RequestMapping("/logout")
    public String logout(){
        //A. 清空会话中用户数据
        session.removeAttribute("loginInfo");
        //B. 销毁session
        session.invalidate();
        //C. 跳转到登陆页面
        return "forward:/login.jsp";
    }
}
