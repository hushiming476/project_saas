package cn.itcast.service.system;


import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
   用户的业务接口
 */
public interface UserService {

    /**
       分页查询
     @param pageNum 当前页
     @param pageSize 页大小
     */
    PageInfo<User> findByPage(String companyId,int pageNum,int pageSize);

    /**
      根据用户主键ID查询
     */
    User findById(String id);

    /**
       查询全部用户
     @param companyId 根据部门的ID
     */
    List<User> findAll(String companyId);


    /**
       添加用户
     */
    void save(User user);

    /**
       修改用户
     */
    void update(User user);

    /**
      删除用户
     @param id
     */
    boolean delete(String id);

    /**
     * 用户分配角色
     * @param userId  用户id
     * @param roleIds 分配的多个角色id
     */
    void changeRole(String userId, String[] roleIds);

    /**

     */
    User findByEmail(String email);
}
