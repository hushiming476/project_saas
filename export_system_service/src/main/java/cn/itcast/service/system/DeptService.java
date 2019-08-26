package cn.itcast.service.system;


import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
   部门的业务接口
 */
public interface DeptService {

    /**
       分页查询
     @param pageNum 当前页
     @param pageSize 当前页大小
     */
    PageInfo<Dept> findByPage(String id, int pageNum, int pageSize);

    /**
       主键查询
     */
    Dept findById(String id);

   /**
      查询全部部门
    @param companyId 根据企业的ID
    */
   List<Dept> findAll(String companyId);

    /**
      添加部门
     @param dept
     */
    void save(Dept dept);

    /**
      修改部门
     @param dept
     */
    void update(Dept dept);

    /**
       删除部门
     */
    boolean delete(String id);
}
