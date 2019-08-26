package cn.itcast.web.aspect;
import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * 日志切面类
 */
@Component  // 创建对象加入容器
@Aspect     // 指定当前类为切面类/通知类
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 需求：再执行控制器的方法之后自动记录日志
     * @param pjp 连接点对象； 可以获取当前执行的方法信息、目标对象
     */
    @Around("execution(* cn.itcast.web.controller.*.*.*(..))")
    public Object insertLog(ProceedingJoinPoint pjp){

        SysLog sysLog = new SysLog();
        sysLog.setId(UUID.randomUUID().toString());
        sysLog.setTime(new Date());
        // 设置来访者ip
        sysLog.setIp(request.getRemoteAddr());
        // 设置当前执行的方法信息
        sysLog.setMethod(pjp.getSignature().getName());
        sysLog.setAction(pjp.getTarget().getClass().getName());

        // 从session获取登陆用户信息
        User user = (User) request.getSession().getAttribute("loginInfo");
        // 判断
        if(user != null) {
            sysLog.setUserName(user.getUserName());
            sysLog.setCompanyId(user.getCompanyId());
            sysLog.setCompanyName(user.getCompanyName());
        }

        try {
            // 执行控制器方法
            Object retV = pjp.proceed();
            // 记录日志
            sysLogService.save(sysLog);
            return retV;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
