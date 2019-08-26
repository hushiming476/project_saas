package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 出口报运
 */
@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    // 注入购销合同业务类
    @Reference
    private ContractService contractService;
    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;



    /**
     * 查看  合同管理页面
     * 1. 合同管理，显示已上报待报运的购销合同。co_contract的status=1
     * 请求地址：http://localhost:8080/cargo/export/contractList.do
     */
    @RequestMapping("/contractList")
    public String contractList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize){
        // 1. 构造合同查询条件
        ContractExample example = new ContractExample();
        // 根据create_time 进行降序
        example.setOrderByClause("create_time desc");

        //查询条件对象
        ContractExample.Criteria criteria =example.createCriteria();
        // 查询条件：企业id
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        // 按照购销合同的状态查询
        criteria.andStateEqualTo(1);

        //调用service 分页查询
        PageInfo<Contract> pageInfo =
                contractService.findByPage(example, pageNum, pageSize);
        // 返回
        request.setAttribute("pageInfo",pageInfo);
        // 转发：合同管理页面
        return "cargo/export/export-contractList";

    }

    /**
     *  2. 出口报运单列表
     *  list.do
     */
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "5")Integer pageSize){
        // 构造条件
        ExportExample  exportExample = new ExportExample();
        ExportExample.Criteria criteria = exportExample.createCriteria();
        // 条件：企业id
        criteria.andCompanyIdEqualTo(getLoginCompanyId());

        // 调用service
        PageInfo<Export> pageInfo = exportService.findByPage(exportExample, pageNum, pageSize);

        // 保存
        request.setAttribute("pageInfo",pageInfo);
        // 返回  报运单列表
        return "cargo/export/export-list";
    }


    /**
     * 3. 添加报运单（1）进入添加页面
     * 请求地址：http://localhost:8080/cargo/export/toExport.do
     * 请求参数：
     *      id  购销合同id
     *      id  购销合同id
     * 如何封装请求数据？
     *      方式1：public String toExport(String id){}    自动逗号隔开
     *      方式2：public String toExport(String[] id){}
     *
     */
    @RequestMapping("/toExpoet")
    public String toExport(String id){

        request.setAttribute("id",id);
        // 返回
        return "cargo/export/export-toExport";
    }

    /**
     * 4. 添加报运单/修改报运单
     */
    @RequestMapping("/edit")
    public String edit(Export export){
        // 设置报运单所属企业id、名称
        export.setCompanyId(getLoginCompanyId());
        export.setCompanyName(getLoginCompanyName());

        // 判断
        if (StringUtils.isEmpty(export.getId())){
            // 如果id为空--添加
            exportService.save(export);
        }else {
            // 修改
            exportService.update(export);
        }
        return "redirect:/cargo/export/list.do";

    }

    /**
     * 5. 进入报运单修改页面
     * http://localhost:8080/cargo/export/toUpdate.do?id=0
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){

        // 根据报运单id 查询
        Export export = exportService.findById(id);

        // 根据报运单id, 查询报运的商品
        ExportProductExample epExample = new ExportProductExample();
        epExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> eps = exportProductService.findAll(epExample);

        // 保存，回显数据
        request.setAttribute("export",export);
        // 保存商品
        request.setAttribute("eps",eps);
        // 返回
        return "cargo/export/export-update";

    }


}
