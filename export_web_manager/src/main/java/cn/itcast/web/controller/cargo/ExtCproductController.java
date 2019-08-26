package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController{

    @Reference
    private ExtCproductService extCproductService;
    @Reference
    private FactoryService factoryService;

    /**
     * 1. 附件列表与添加页面
     * 需求：
     *      查询货物的附件
     * 功能入口：
     *      购销合同--->货物----> 点击附件
     * 请求地址：
     *      http://localhost:8080/cargo/extCproduct/list.do
     * 请求参数：
     *      contractId              购销合同
     *      contractProductId       货物
     */
    @RequestMapping("/list")
    public String list(String contractId,String contractProductId,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize){
        //1.1 查询工厂
        FactoryExample factoryExample = new FactoryExample();
        // 查询条件：工厂类型ctype = 货物
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //1.2 根据货物id查询附件
        // 构造查询条件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(contractProductId);
        PageInfo<ExtCproduct> pageInfo =
                extCproductService.findByPage(extCproductExample, pageNum, pageSize);

        //1.3 保存数据
        request.setAttribute("factoryList",factoryList);
        request.setAttribute("pageInfo",pageInfo);
        // 注意：这里需要保存购销合同id，主要是为了后面添加货物时候，要指定购销合同id
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);
        //1.4 返回
        return "cargo/extc/extc-list";
    }

    /**
     * 2. 附件添加、修改
     */
    @RequestMapping("/edit")
    public String edit(ExtCproduct extCproduct){
        // 设置部门所属企业id、名称
        extCproduct.setCompanyId(getLoginCompanyId());
        extCproduct.setCompanyName(getLoginCompanyName());
        // 判断
        if (StringUtils.isEmpty(extCproduct.getId())){
            // 添加
            extCproductService.save(extCproduct);
        } else {
            // 修改
            extCproductService.update(extCproduct);
        }
        return "redirect:/cargo/extCproduct/list.do?contractId="+
                extCproduct.getContractId() + "&contractProductId=" +
                extCproduct.getContractProductId();
    }

    /**
     * 3. 附件修改（1）进入修改页面
     * 请求地址：http://localhost:8080/cargo/extCproduct/toUpdate.do?id=0
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //3.1 根据附件id查询
        ExtCproduct extCproduct = extCproductService.findById(id);

        //3.2 查询附件工厂
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //3.3 保存
        request.setAttribute("extCproduct",extCproduct);
        request.setAttribute("factoryList",factoryList);

        return "cargo/extc/extc-update";
    }

    /**
     * 4. 删除附件
     * 请求地址：http://localhost:8080/cargo/extCproduct/delete.do
     * 请求参数：
     *      id                  附件id
     *      contractId          购销合同id 【为了重定向到列表】
     *      contractProductId   货物id    【为了重定向到列表】
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId,String contractProductId){
        // 调用service删除
        extCproductService.delete(id);
        return "redirect:/cargo/extCproduct/list.do?contractId="+
                contractId + "&contractProductId=" + contractProductId;
    }
}
