package cn.itcast.service.system.impl;

import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    // 注入dao
    @Autowired
    private RoleDao roleDao;

    @Override
    public PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize) {
        // 开启查询分页支持
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao查询
        List<Role> list = roleDao.findAll(companyId);
        return new PageInfo<>(list);
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public List<Role> findAll(String companyId) {
        return roleDao.findAll(companyId);
    }

    @Override
    public void save(Role role) {
        role.setId(UUID.randomUUID().toString());
        roleDao.save(role);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public void delete(String id) {
        roleDao.delete(id);
    }

    // 角色分配权限
    @Override
    public void updateRoleModule(String roleId, String moduleIds) {
        //-- 1) 先解除角色权限的关系
        //DELETE FROM pe_role_module WHERE role_id=?
        //-- 2) 角色添加权限
        //INSERT INTO pe_role_module(role_id,module_id)VALUES(?,?)

        //1) 先解除角色权限的关系
        roleDao.deleteRoleModuleByRoleId(roleId);

        //2) 角色添加权限
        //A. 判断
        if (moduleIds != null && !"".equals(moduleIds)){
            //B. 分割字符串
            String[] array = moduleIds.split(",");
            //C. 判断
            if (array != null && array.length > 0){
                // D. 遍历权限ID数组
                for (String moduleId : array) {
                    //E. 角色添加权限
                    roleDao.saveRoleModule(roleId,moduleId);
                }
            }
        }
    }

    @Override
    public List<Role> findRoleByUserId(String id) {
        return roleDao.findRoleByUserId(id);
    }


}
