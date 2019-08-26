package cn.itcast.service.system;

import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService {
    /**
     * 分页查询部门
     * @param companyId 根据部门的所属企业分页查询
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return 返回分页对象
     */
    PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize);

    /**
     * 主键查询
     */
    Role findById(String id);

    /**
     * 查询全部
     * @param companyId
     * @return
     */
    List<Role> findAll(String companyId);

    /**
     * 添加
     * @param role
     */
    void save(Role role);

    /**
     * 修改
     * @param role
     */
    void update(Role role);

    /**
     * 删除
     * @param id 根据id删除
     * @return 删除成功返回true
     */
    void delete(String id);

    /**
     * 角色分配权限
     * @param roleId 角色id
     * @param moduleIds 多个权限id，用逗号隔开
     */
    void updateRoleModule(String roleId, String moduleIds);


    /**
     从用户列表进入角色页面
     @param id
     */
    List<Role> findRoleByUserId(String id);
}
