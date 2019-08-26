package cn.itcast.service.system.impl;


import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    // 注入dao
    @Autowired
    private UserDao userDao;

    // package cn.itcast.service.system.impl;
    @Override
    public PageInfo<User> findByPage(String companyId, int pageNum, int pageSize) {
        // 开启查询分页支持
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao查询
        List<User> list = userDao.findAll(companyId);
        return new PageInfo<>(list);
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll(String companyId) {
        return userDao.findAll(companyId);
    }

    @Override
    public void save(User user) {
        user.setId(UUID.randomUUID().toString());

        userDao.save(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    //SELECT COUNT(1) FROM pe_role_user WHERE user_id='0'
    //DELETE FROM pe_user WHERE user_id='0'
    @Override
    public boolean delete(String id) {
        // 先根据删除的用户id查询
        Long count = userDao.findUserRoleById(id);

        // 判断
        if (count == null || count == 0){
            // 可以删除
            userDao.delete(id);
            return true;
        }
        return false;
    }

    @Override
    public void changeRole(String userId, String[] roleIds) {

       //用户分配角色
       //1) 解除用户角色关系
       //DELETE FROM pe_role_user WHERE user_id=''
       //2) 用户添加角色
       //INSERT INTO pe_role_user(user_id,role_id)VALUES('','')

        //1) 解除用户角色关系
        userDao.deleteUserRoleByUserId(userId);

        //2) 用户添加角色
        if (roleIds != null && roleIds.length > 0){
            for (String roleId : roleIds) {
                userDao.saveUserRole(userId,roleId);
            }
        }
    }


    @Override
    public User findByEmail(String email) {
        List<User> list = userDao.findByEmail(email);
        return list!=null&&list.size()>0 ? list.get(0) : null;
    }


}

















