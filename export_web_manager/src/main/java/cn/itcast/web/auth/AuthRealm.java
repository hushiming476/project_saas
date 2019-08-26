package cn.itcast.web.auth;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1. 获取登陆的用户名(邮箱)
        String email = (String) token.getPrincipal();

        //2. 根据email查询
        User user = userService.findByEmail(email);

        //3. 判断
        if (user == null) {
            // 一旦认证方法返回NULL，就是UnknownAccountException异常
            return null;
        }

        //4. 获取数据库中正确的密码
        String dbPwd = user.getPassword();

        //5. 返回
        // 参数1：身份对象。通过subject.getPrincipal(); 获取的就是这里的参数1
        // 参数2：数据库中正确的密码
        // 参数3：realm名称，可以随意。唯一。getName()获取默认名称
        SimpleAuthenticationInfo sai =
                new SimpleAuthenticationInfo(user,dbPwd,this.getName());

        return sai;
    }

    // 授权: 返回用户拥有的权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 1. 获取认证后的身份对象 (realm认证方法返回对象的构造函数的第一个参数)
        User user = (User) principals.getPrimaryPrincipal();

        // 2. 根据用户id查询用户的权限
        List<Module> moduleList = moduleService.findModuleByUserId(user.getId());

        // 3. 返回
        SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
        // 遍历用户的权限集合
        if (moduleList != null && moduleList.size() > 0) {
            for (Module module : moduleList) {
                // 返回用户的权限
                sai.addStringPermission(module.getName());
            }
        }
        return sai;
    }
}
