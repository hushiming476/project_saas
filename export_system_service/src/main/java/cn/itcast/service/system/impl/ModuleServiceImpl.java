package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {
// D:\Project\project_01\export_parent\export_system_service\src\main\java\cn\itcast\service\company\impl\system\impl\ModuleServiceImpl.java
    // 注入dao
    @Autowired
    private ModuleDao moduleDao;
    @Autowired
    private UserDao userDao;

    @Override
    public PageInfo<Module> findByPage(int pageNum, int pageSize) {
        // 开启查询分页支持
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao查询
        List<Module> list = moduleDao.findAll();
        return new PageInfo<>(list);
    }

    @Override
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    @Override
    public void save(Module module) {
        module.setId(UUID.randomUUID().toString());
        moduleDao.save(module);
    }

    @Override
    public void update(Module module) {
        moduleDao.update(module);
    }

    @Override
    public void delete(String id) {
        moduleDao.delete(id);
    }

    @Override
    public List<Module> findModulesByRoleId(String roleId) {
        return moduleDao.findModulesByRoleId(roleId);
    }

    @Override
    public List<Module> findModuleByUserId(String userId) {
        //1. 根据用户id查询
        User user = userDao.findById(userId);

        /**
         * 2. 获取用户级别
         *      0-saas管理员
         *      1-企业管理员
         *      2-管理所有下属部门和人员
         *      3-管理本部门
         *      4-普通员工
         */
        Integer degree = user.getDegree();

        //3. 判断：根据登陆用户的degree判断用户级别
        if (degree==0){
            //1） 登陆用户的degree=0，说明是Saas管理员。可以查看Saas模块（belong=0）
            //SELECT * FROM ss_module WHERE belong=0
            return moduleDao.findByBelong(0);
        }
        else if (degree == 1){
            //2)  登陆用户的degree=1，说明是企业管理员。可以查看所有模块，除了Saas以外。（belong=1）
            //SELECT * FROM ss_module WHERE belong=1
            return moduleDao.findByBelong(1);
        } else {
            //3） 其他用户：根据用户查询用户的权限。（用户角色、角色权限表、权限表）
            return moduleDao.findModuleByUserId(userId);
        }
    }
}
