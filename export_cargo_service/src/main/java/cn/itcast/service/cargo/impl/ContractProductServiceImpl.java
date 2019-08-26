package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.domain.vo.ContractProductVo;
import cn.itcast.service.cargo.ContractProductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * 购销合同货物模块服务接口实现
 * import com.alibaba.dubbo.config.annotation.Service;
 */
@Service
public class ContractProductServiceImpl implements ContractProductService {

    // 注入dao
    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public PageInfo<ContractProduct> findByPage(ContractProductExample contractProductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ContractProduct> list = contractProductDao.selectByExample(contractProductExample);
        return new PageInfo<>(list);
    }

    @Override
    public List<ContractProduct> findAll(ContractProductExample contractProductExample) {
        return contractProductDao.selectByExample(contractProductExample);
    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    // 添加货物
    @Override
    public void save(ContractProduct contractProduct) {
        //0. 设置id
        contractProduct.setId(UUID.randomUUID().toString());

        //1. 计算货物金额
        Double amount = 0d;
        if (contractProduct.getCnumber() != null &&
                contractProduct.getPrice() != null) {
            amount = contractProduct.getCnumber() * contractProduct.getPrice();
        }
        contractProduct.setAmount(amount);

        //2. 修改购销合同总金额、货物数
        //2.1 根据购销合同id查询
        Contract contract =
                contractDao.selectByPrimaryKey(contractProduct.getContractId());

        //2.2 修改购销合同总金额
        contract.setTotalAmount(contract.getTotalAmount() + amount);

        //2.3 修改购销合同货物数量要加1
        contract.setProNum(contract.getProNum() + 1);

        //2.4 保存购销合同
        contractDao.updateByPrimaryKeySelective(contract);

        //3. 添加货物
        contractProductDao.insertSelective(contractProduct);
    }

    @Override
    public void update(ContractProduct contractProduct) {
        //1. 计算修改后的货物金额
        Double amount = 0d;
        if (contractProduct.getCnumber() != null &&
                contractProduct.getPrice() != null) {
            amount = contractProduct.getCnumber() * contractProduct.getPrice();
        }
        contractProduct.setAmount(amount);

        // 获取修改前的货物金额： 需要查询数据库
        ContractProduct cp =
                contractProductDao.selectByPrimaryKey(contractProduct.getId());
        // 获取修改前货物金额
        Double oldAmount = cp.getAmount();

        //2. 修改购销合同总金额
        //2.1 根据购销合同id查询
        Contract contract =
                contractDao.selectByPrimaryKey(contractProduct.getContractId());

        //2.2 修改购销合同总金额
        contract.setTotalAmount(contract.getTotalAmount() + amount - oldAmount);

        //2.3 修改购销合同
        contractDao.updateByPrimaryKeySelective(contract);

        //3. 修改货物
        contractProductDao.updateByPrimaryKeySelective(contractProduct);
    }

    // 参数： 货物id
    @Override
    public void delete(String id) {
        //1. 根据货物id查询
        ContractProduct contractProduct =
                contractProductDao.selectByPrimaryKey(id);

        //2. 【获取货物金额】
        Double amount = contractProduct.getAmount();

        //3. 【获取附件金额】 【删除附件】
        //3.1 根据货物id，查询附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(id);
        List<ExtCproduct> extCproductList =
                extCproductDao.selectByExample(extCproductExample);

        //3.2 遍历附件： 获取附件金额、删除附件
        Double extcAmount = 0d;
        if (extCproductList != null && extCproductList.size()>0) {
            for (ExtCproduct extCproduct : extCproductList) {
                // 累加附件金额
                extcAmount += extCproduct.getAmount();
                // 删除附件
                extCproductDao.deleteByPrimaryKey(extCproduct.getId());
            }
        }

        //4. 修改购销合同： 总金额、货物数、附件数
        Contract contract =
                contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount()-amount-extcAmount);
        contract.setProNum(contract.getProNum()-1);
        contract.setExtNum(contract.getExtNum()-extCproductList.size());
        // 修改保存
        contractDao.updateByPrimaryKeySelective(contract);

        //5. 删除货物
        contractProductDao.deleteByPrimaryKey(id);
    }

    // 购销合同货物模块服务接口实现
    @Override
    public List<ContractProductVo> findByShipTime(String companyId, String shipTime) {
        return contractProductDao.findByShipTime(companyId,shipTime);
    }
}
