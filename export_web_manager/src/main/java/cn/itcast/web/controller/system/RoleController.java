package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController{

    // 注入service
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModuleService moduleService;


    /**
     * 1. 列表分页查询
     * 请求地址：http://localhost:8080/system/role/list.do
     */
    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize){

        // 分页查询要根据企业id查询，而企业id是根据登陆用户获取其对应的企业id
        // 现在先写死
        String companyId = getLoginCompanyId();

        // 调用service查询
        PageInfo<Role> pageInfo =
                roleService.findByPage(companyId, pageNum, pageSize);
        //返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("system/role/role-list");
        return mv;
    }

    /**
     * 3.添加（1） 进入添加页面
     * 请求地址：http://localhost:8080/system/role/toAdd.do
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "system/role/role-add";
    }

    /**
     * 4. 添加/修改
     * 请求地址：http://localhost:8080/system/role/edit.do
     */
    @RequestMapping("/edit")
    public String edit(Role role){
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();
        // 设置所属企业id、名称
        role.setCompanyId(companyId);
        role.setCompanyName(companyName);
        // 判断
        if (StringUtils.isEmpty(role.getId())){
            // 添加
            roleService.save(role);
        } else {
            // 修改
            roleService.update(role);
        }
        return "redirect:/system/role/list.do";
    }

    /**
     * 5. 进入修改页面
     * 请求地址：http://localhost:8080/system/role/toUpdate.do?id=0
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        String companyId = getLoginCompanyId();
        // 根据id查询
        Role role = roleService.findById(id);

        // 返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("role",role);
        mv.setViewName("system/role/role-update");
        return mv;
    }

    /**
     * 6. 删除
     */
    @RequestMapping("/delete")
    public String delete(String id){
        roleService.delete(id);
        return "redirect:/system/role/list.do";
    }

    /**
     * 7.角色分配权限（1） 进入角色权限页面
     * 功能入口： 角色列表，点击权限
     * 请求地址： http://localhost:8080/system/role/roleModule.do?roleid=4
     */
    @RequestMapping("/roleModule")
    public String roleModule(String roleid){
        // 根据角色id查询角色对象
        Role role = roleService.findById(roleid);
        // 保存
        request.setAttribute("role",role);
        return "system/role/role-module";
    }

    /**
     * 7.角色分配权限（2） role-module.jsp页面发送ajax请求，返回ztree需要的json格式数据
     * 请求地址：/system/role/getZtreeNodes.do
     * 请求参数：roleId=2
     * 响应数据：
     *   json = [
     *      { id:2, pId:0, name:"随意勾选 2", checked:true, open:true},
     *   ]
     */
    @RequestMapping("/getZtreeNodes")
    @ResponseBody       // 返回json格式数据
    public List<Map<String,Object>> getZtreeNodes(String roleId){
        //1. 返回的集合
        List<Map<String,Object>> result = new ArrayList<>();

        //2. 查询所有权限
        List<Module> moduleList = moduleService.findAll();

        //3. 查询当前角色已经拥有的权限 （checked:true）
        List<Module> roleModuleList = moduleService.findModulesByRoleId(roleId);

        //4. 遍历所有权限，构造json格式数据
        if (moduleList != null && moduleList.size() > 0) {
            for (Module module : moduleList) {
                //4.1 构造map
                Map<String,Object> map = new HashMap<>();
                //4.2 封装map
                map.put("id",module.getId());
                map.put("pId",module.getParentId());
                map.put("name",module.getName());
                map.put("open",true);

                //4.2 判断：当前角色如果已经拥有遍历的权限，就设置checked：true
                if (roleModuleList.contains(module)){
                    map.put("checked",true);
                }

                //4.4 map添加到集合
                result.add(map);
            }
        }

        //5. 返回封装的数据：所有权限、角色已经拥有的权限
        return result;
    }

    /**
     * 8.角色分配权限（3） 给用户分配权限
     * @return
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleId,String moduleIds){
        // 调用service，实现角色分配权限
        roleService.updateRoleModule(roleId,moduleIds);
        return "redirect:/system/role/list.do";
    }

}
