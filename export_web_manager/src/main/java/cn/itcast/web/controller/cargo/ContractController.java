package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


/**
 *  购销合同的CRUD
 */
@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    // 注入购销合同的服务接口
    @Reference
    private ContractService contractService;


    /**
     * 1. 列表分页查询(查看合同管理)
     */
    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize){

        //1.1 构造查询条件
        ContractExample example = new ContractExample();
        // 根据create_time进行降序
        example.setOrderByClause("create_time desc");

        //1.2 查询条件对象
        ContractExample.Criteria criteria = example.createCriteria();
        //1.3 查询条件: 企业id
        criteria.andCompanyIdEqualTo(getLoginCompanyId());

        /**
         * 细粒度权限控制，根据用户的degree等级判断，不同的级别显示不同的购销合同数据
         * degree  级别
         *      0-saas管理员
         *      1-企业管理员
         *      2-管理所有下属部门和人员
         *      3-管理本部门
         *      4-普通员工
         */
        User user = getLoginUser();
        if (user.getDegree() == 4){
            // 说明是普通员工： 只能查询自己创建的购销合同
            criteria.andCreateByEqualTo(user.getId());
        }
        else if (user.getDegree() == 3){
            // 说明是部门经理，可以查看本部门下所有员工创建的购销合同
            criteria.andCreateDeptEqualTo(user.getDeptId());
        }
        else if (user.getDegree() == 2){
            // 根据当前登陆用户的部门id，作为条件查询当前部门的子孙部门创建的购销合同。
            PageInfo<Contract> pageInfo =
                    contractService.selectByDeptId(user.getDeptId(), pageNum, pageSize);
            // 返回
            ModelAndView mv = new ModelAndView();
            mv.addObject("pageInfo",pageInfo);
            mv.setViewName("cargo/contract/contract-list");
            return mv;
        }


        //1.2 调用service查询
        PageInfo<Contract> pageInfo =
                contractService.findByPage(example,pageNum,pageSize);
        //返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("cargo/contract/contract-list");
        return mv;
    }

    /**
     * 2.添加（1） 进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "cargo/contract/contract-add";
    }

    /**
     * 3. 添加/修改
     */
    @RequestMapping("/edit")
    public String edit(Contract contract){
        // 设置所属企业id、名称
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());
        // 判断
        if (StringUtils.isEmpty(contract.getId())){
            /*细粒度的权限控制*/
            // 设置创建者
            contract.setCreateBy(getLoginUser().getId());
            // 设置创建者所属部门
            contract.setCreateDept(getLoginUser().getDeptId());

            // 添加
            contractService.save(contract);
        } else {
            // 修改
            contractService.update(contract);
        }
        return "redirect:/cargo/contract/list.do";
    }
    /**
     * 4. 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        // 根据id查询
        Contract contract = contractService.findById(id);
        // 保存
        request.setAttribute("contract",contract);
        // 转发到页面
        return "cargo/contract/contract-update";
    }

    /**
     * 5. 删除
     */
    @RequestMapping("/delete")
    public String delete(String id){
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 6. 购销合同：查看、提交、取消
     * 查看 http://localhost:8080/cargo/contract/toView.do?id=3
     * 提交 http://localhost:8080/cargo/contract/submit.do?id=3
     * 取消 http://localhost:8080/cargo/contract/cancel.do?id=3
     */
    @RequestMapping("/toView")
    public String toView(String id){
        Contract contract = contractService.findById(id);
        request.setAttribute("contract",contract);
        return "cargo/contract/contract-view";
    }

    @RequestMapping("/submit")
    public String submit(String id){
        // 提交：状态修改为1
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(1);
        // 修改（动态更新：对象属性有值，才生成更新sql）
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping("/cancel")
    public String cancel(String id){
        // 取消：状态修改为0
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(0);
        // 修改（动态更新：对象属性有值，才生成更新sql）
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }
}
