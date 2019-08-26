package cn.itcast.web.controller.system;


import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;

    /**
      1.查询所有用户
     */
    @RequestMapping("/list")
    @RequiresPermissions("用户管理")
    public String list
    (@RequestParam(defaultValue = "1")Integer pageNum,
     @RequestParam(defaultValue = "5")Integer pageSize){

        String companyId = getLoginCompanyId();
        PageInfo<User> pageInfo =
                userService.findByPage(companyId, pageNum, pageSize);
        //返回列表
       request.setAttribute("pageInfo",pageInfo);
        return "system/user/user-list";

    }


    /**
        2.进入用户添加页面
        请求地址：toAdd.do
     */
    @RequestMapping("/toAdd")
    public ModelAndView toAdd(){
        String companyId = getLoginCompanyId();

        List<Dept> deptList = deptService.findAll(companyId);

        ModelAndView mv = new ModelAndView();
        mv.addObject("deptList",deptList);
        mv.setViewName("system/user/user-add");
        return mv;
    }

    /**
       3.添加用户/修改用户
       请求地址：edit.do
       思路：1.UUID:id不能重复，2.判断是否为空，决定添加还是修改
       重定向
     */
    // 注入RabbitTemple
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/edit")
    public String edit(User user){
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();

        //设置用户所属企业的id，名称
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);

        //判断
        if (StringUtils.isEmpty(user.getId())){

            //添加
            userService.save(user);

            // 发送消息到消息行列
            Map<String,Object> map = new HashMap<>();
            map.put("email",user.getEmail());
            map.put("subject","day14天，好好学习，日如过万代码。");
            map.put("context","加油吧，小伙子们！");
            // 发信息
            rabbitTemplate.convertAndSend("msg.email",map);


        }else{
            //修改
            userService.update(user);
        }
        return "redirect:/system/user/list.do";
    }

    /**
      4.修改用户页面
      请求地址：toUpdate
      思路：先查看有哪些用户再修改
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        String companyId = getLoginCompanyId();
        User user = userService.findById(id);
        List<Dept> deptList = deptService.findAll(companyId);

        //返回
        ModelAndView mv= new ModelAndView();
        mv.addObject("user",user);
        mv.addObject("deptList",deptList);
        mv.setViewName("/system/user/user-update");
        return mv;
    }

    /**
     5. 删除用户
     请求地址：delete.do
     请求参数：id=?
     响应参数
     */
    @RequestMapping("/delete")
    @ResponseBody   // 返回json
    public Map<String,String> delete(String id){
        //1. 返回的对象
        Map<String,String> result = new HashMap<>();
        //2. 调用service： 返回true表示删除成功
        boolean flag = userService.delete(id);
        //3. 判断
        if (flag){
            result.put("message","删除成功");
        } else {
            result.put("message","删除失败: 当前用户有被其他数据引用！");
        }
        return result;
    }

    /**
       6.从用户列表进入角色页面
       请求地址：http://localhost:8080/system/user/roleList.do?id=00
     */
    @RequestMapping("/roleList")
    public String roleList(String id){
        //A. 根据用户id查询
        User user = userService.findById(id);

        //B. 查询所有角色
        List<Role> roleList = roleService.findAll(getLoginCompanyId());

        //C. 根据用户id查询用户拥有的角色
        List<Role> userRoleList =  roleService.findRoleByUserId(id);
        // 定义一个角色字符串，保存用户的所有角色。逗号隔开多个角色
        String roleStr = "";
        // 遍历用户角色
        if (userRoleList!=null && userRoleList.size()>0) {
            for (Role role : userRoleList) {
                roleStr += role.getId() + ",";
            }
        }

        //D. 保存
        request.setAttribute("user",user);
        request.setAttribute("roleList",roleList);
        request.setAttribute("userRoleStr",roleStr);
        return "system/user/user-role";
    }

        /**
         7. 修改用户角色
          请求地址：http://localhost:8080/system/user/changeRole.do
         */
        @RequestMapping("/changeRole")
        public String changeRole(String userid,String[] roleIds){
            // 用户分配角色
            userService.changeRole(userid,roleIds);
            return "redirect:/system/user/list.do";
        }

}
