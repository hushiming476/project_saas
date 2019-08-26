package cn.itcast.web.controller.company;


import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {

    // 注入service
   @Reference
    private CompanyService companyService;
      //1.企业列表
    @RequestMapping("/list")
    public ModelAndView list(){
        List<Company> list = companyService.findAll();
        ModelAndView mv = new ModelAndView();
        mv.addObject("list",list);
       mv.setViewName("company/company-list");
        return mv;
    }


     /**
        3.添加企业：
        功能入口：列表点击添加
        请求地址：toAdd.to
        响应地址：company-add.jsp
      */
     @RequestMapping("/toAdd")
    public String toAdd(){
         return "company/company-add";
     }

     /**
        4.添加企业/修改企业
          请求地址：edit.do
          请求参数：id/name:企业名称/address:地址/. ......
      */
     @RequestMapping("/edit")
    public String edit(Company company){
     //判断id是否为空,决定修改还是添加
         if (StringUtils.isEmpty(company.getId())){
             companyService.save(company);
         }else {
             companyService.update(company);
         }
        //重定向
         return "redirect:/company/list.do";
     }

     /**
        5.修改企业
          请求地址：toUpdate.do
          请求参数：ID=3......
          响应地址：update.jsp
      */
     @RequestMapping("/toUpdate")
    public String toUpdate(String id,Model model){
        // 思路1：要先根据ID查询到:findById，才能修改
         Company company = companyService.findById(id);
         //思路2：查询到后，往request域中添加修改后的数据
        model.addAttribute("company",company);
        return "company/company-update";
     }

     /**
       6.删除企业
         请求地址：delete.do
      */
     @RequestMapping("/delete")
     public String delete(String id){
         companyService.delete(id);
         //思路：删除完后要返回哪一个页面
         return "redirect:/company/list.do";
     }

}
