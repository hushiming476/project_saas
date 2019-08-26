package cn.itcast.web.controller.system;


import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/system/dept")
public class DeptController extends BaseController {


    @Autowired
    private DeptService deptService;


    /**
       1.查询
       先把当前页和页数大小写死（就要设置默认值defaultValue）
       例如：@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize )
       部门列表分页查询，返查询完后回列表
     */
    @RequestMapping("/list")
   public ModelAndView list(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize ){

        //根据企业ID分页查询所有部门
        String companyId = getLoginCompanyId();
        PageInfo<Dept> pageInfo = deptService.findByPage(companyId, pageNum, pageSize);

        //并且返回列表
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("system/dept/dept-list");
        return mv;
    }

    /**
       2.进入部门添加页面：
       请求地址是：toAdd.do(看dept-list.jsp)
       思路：使用 ModelAndView
             获取登陆用户的所属企业id
             调用业务层deptService查看，全部部门，
             并把新添加的部门添加到全部部门中
             并返回添加页面
     */
    @RequestMapping("/toAdd")
    public ModelAndView toAdd(){
        String companyId = getLoginCompanyId();
        List<Dept> deptList = deptService.findAll(companyId);

        ModelAndView mv= new ModelAndView();
        mv.setViewName("system/dept/dept-add");
        mv.addObject("deptList",deptList);
        return mv;
    }

    /**
       3.添加部门/修改部门
       请求地址：dept-add.jsp： action="/system/dept/edit.do"
       思路：UUID,判断是否为空
       重定向
     */
    @RequestMapping("/edit")
    public String edit(Dept dept){
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();
        // 设置部门所属企业id、名称
        dept.setCompanyId(companyId);
        dept.setCompanyName(companyName);
        // 判断
        if (StringUtils.isEmpty(dept.getId())){
            // 添加
            deptService.save(dept);
        } else {
            // 修改
            deptService.update(dept);
        }
        return "redirect:/system/dept/list.do";
    }

    /**
     4. 修改页面
      请求地址：toUpdate
     思路：先查询（全部部门），再修改
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        String companyId = getLoginCompanyId();
        Dept dept = deptService.findById(id);
        List<Dept> deptList = deptService.findAll(companyId);

        ModelAndView mv = new ModelAndView();
        mv.addObject("dept",dept);
        mv.addObject("deptList",deptList);
        mv.setViewName("system/dept/dept-update");
        return mv;
    }

    /**
      5.删除部门：
       请求地址：delete.do
      请求参数：id=?
      响应数据：删除成功还是失败
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,String> delete(String id){
        Map<String,String> result = new HashMap<>();
        boolean flag = deptService.delete(id);
        if (flag){
            result.put("message","删除成功");
        }else{
            result.put("message","删除失败！");
        }
        return result;
    }
}
