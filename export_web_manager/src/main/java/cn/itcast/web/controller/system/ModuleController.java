package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.service.system.ModuleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController{

    // 注入service
    @Autowired
    private ModuleService moduleService;


    /**
     * 1. 列表分页查询
     * 请求地址：http://localhost:8080/system/module/list.do
     */
    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize){
        // 调用service查询
        PageInfo<Module> pageInfo =
                moduleService.findByPage(pageNum, pageSize);
        //返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("system/module/module-list");
        return mv;
    }

    /**
     * 3.添加（1） 进入添加页面
     * 请求地址：http://localhost:8080/system/module/toAdd.do
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        // 查询所有模块，作为父菜单
        List<Module> list = moduleService.findAll();
        request.setAttribute("menus",list);
        return "system/module/module-add";
    }

    /**
     * 4. 添加/修改
     * 请求地址：http://localhost:8080/system/module/edit.do
     */
    @RequestMapping("/edit")
    public String edit(Module module){
        // 判断
        if (StringUtils.isEmpty(module.getId())){
            // 添加
            moduleService.save(module);
        } else {
            // 修改
            moduleService.update(module);
        }
        return "redirect:/system/module/list.do";
    }

    /**
     * 5. 进入修改页面
     * 请求地址：http://localhost:8080/system/module/toUpdate.do?id=0
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        // 根据id查询
        Module module = moduleService.findById(id);
        // 查询所有模块，作为父菜单
        List<Module> list = moduleService.findAll();

        // 保存
        request.setAttribute("module",module);
        request.setAttribute("menus",list);

        // 返回
        return "system/module/module-update";
    }

    /**
     * 6. 删除
     */
    @RequestMapping("/delete")
    public String delete(String id){
        moduleService.delete(id);
        return "redirect:/system/module/list.do";
    }
}
