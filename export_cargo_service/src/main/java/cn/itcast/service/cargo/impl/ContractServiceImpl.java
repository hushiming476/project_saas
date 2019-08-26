package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.service.cargo.ContractService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 购销合同模块服务接口实现
 * import com.alibaba.dubbo.config.annotation.Service;
 */
@Service
public class ContractServiceImpl implements ContractService {

    // 注入dao
    @Autowired
    private ContractDao contractDao;

    @Override
    public PageInfo<Contract> findByPage(ContractExample contractExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Contract> list = contractDao.selectByExample(contractExample);
        return new PageInfo<>(list);
    }

    // 合同
    @Override
    public List<Contract> findAll(ContractExample contractExample) {
        return contractDao.selectByExample(contractExample);
    }

    // 根据id查找
    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    // 添加
    @Override
    public void save(Contract contract) {
        // 设置uuid作为主键
        contract.setId(UUID.randomUUID().toString());
        // 记录购销合同创建时间
        contract.setCreateTime(new Date());
        // 默认状态为草稿
        contract.setState(0);


        // 初始化： 总金额为0
        contract.setTotalAmount(0d);
        // 初始化： 货物数、附件数
        contract.setProNum(0);
        contract.setExtNum(0);

        contract.setUpdateTime(new Date());

        contractDao.insertSelective(contract);
    }

    // 修改
    @Override
    public void update(Contract contract) {
        contractDao.updateByPrimaryKeySelective(contract);
    }

    // 删除
    @Override
    public void delete(String id) {
        contractDao.deleteByPrimaryKey(id);
    }

    // 分页查询
    @Override
    public PageInfo<Contract> selectByDeptId(String deptId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(contractDao.selectByDeptId(deptId));
    }







}
