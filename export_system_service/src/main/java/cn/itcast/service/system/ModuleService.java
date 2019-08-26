package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ModuleService {
    /**
     * 分页查询部门
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return 返回分页对象
     */
    PageInfo<Module> findByPage(int pageNum, int pageSize);

    /**
     * 主键查询
     */
    Module findById(String id);

    /**
     * 查询全部
     * @return
     */
    List<Module> findAll();

    /**
     * 添加
     * @param module
     */
    void save(Module module);

    /**
     * 修改
     * @param module
     */
    void update(Module module);

    /**
     * 删除
     * @param id 根据id删除
     * @return 删除成功返回true
     */
    void delete(String id);

    /**
     * 根据角色id查询权限
     * @param roleId
     * @return
     */
    List<Module> findModulesByRoleId(String roleId);

    /**
     * 根据登陆用户id查询用户的权限(动态菜单显示 )
     * 不同用户权限，看到的不一样（sass管理员，企业管理员，普通用户）
     * @param userId
     * @return
     */
    List<Module> findModuleByUserId(String userId);
}
