package cn.itcast.web.controller.system;


import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.SysLogService;
import cn.itcast.service.system.UserService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {


    @Autowired
    private SysLogService sysLogService;

    /**
      1.查询所有用户
     */
    @RequestMapping("/list")
    public String list
    (@RequestParam(defaultValue = "1")Integer pageNum,
     @RequestParam(defaultValue = "5")Integer pageSize){

        String companyId = getLoginCompanyId();
        PageInfo<SysLog> pageInfo =
                sysLogService.findByPage(companyId, pageNum, pageSize);
        //返回列表
       request.setAttribute("pageInfo",pageInfo);
        return "system/log/log-list";

    }

}
