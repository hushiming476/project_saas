package cn.itcast.web.exceptions;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
   统一异常处理类
 */
@Component  //只需要创建对象，加入容器
public class CustomExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException
            (HttpServletRequest request,
             HttpServletResponse response,
             Object handler, Exception ex) {
        //打印错误信息到控制台
        ex.printStackTrace();
        //返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("errorMsg","对不起，系统忙稍后再试！");
        mv.setViewName("error");
        return mv;

    }
}
