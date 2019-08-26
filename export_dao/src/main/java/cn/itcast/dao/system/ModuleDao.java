package cn.itcast.dao.system;

import cn.itcast.domain.system.Module;

import java.util.List;

public interface ModuleDao {

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    void delete(String moduleId);

    //添加
    void save(Module module);

    //更新
    void update(Module module);

    //查询全部
    List<Module> findAll();

    //根据角色id查询权限
    List<Module> findModulesByRoleId(String roleId);

    // 根据belong作为条件查询
    List<Module> findByBelong(int belong);

    // 根据用户查询用户的权限
    List<Module> findModuleByUserId(String userId);
}