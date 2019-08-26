package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 购销合同模块
 */
public interface ContractService {

    /**
     * 1 分页查询
     * @param contractExample 分页查询的参数
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    PageInfo<Contract> findByPage(
            ContractExample contractExample, int pageNum, int pageSize);

    /**
     * 2 查询所有
     */
    List<Contract> findAll(ContractExample contractExample);

    /**
     * 3 根据id查询
     * @param id
     * @return
     */
    Contract findById(String id);

    /**
     * 4 新增
     */
    void save(Contract contract);

    /**
     * 5 修改
     */
    void update(Contract contract);

    /**
     *6  删除部门
     */
    void delete(String id);

    /**
     * 7 根据部门id，查询当前部门及所有子部门下的登陆用户创建的购销合同
     * @param deptId
     * @return
     */
    PageInfo<Contract> selectByDeptId(String deptId,int pageNum, int pageSize);
}











