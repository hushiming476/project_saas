package cn.itcast.dao.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;

import java.util.List;

public interface ContractDao {
    //1 条件查询
    List<Contract> selectByExample(ContractExample example);

    //2 id查询
    Contract selectByPrimaryKey(String id);

    //3 保存
    int insertSelective(Contract record);

    //4 更新
    int updateByPrimaryKeySelective(Contract record);

    //5 删除
    int deleteByPrimaryKey(String id);

    //6 根据部门id，查询当前部门及所有子部门下的登陆用户创建的购销合同
    List<Contract> selectByDeptId(String deptId);
}