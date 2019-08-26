package cn.itcast.web.auth;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    /**
     * 密码匹配判断
     * @param token 通过token获取用户输入的账号密码信息
     * @param info  获取认证后的密码信息。也是数据库中正确的密码
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //1. 获取用户输入的用户名
        String email = (String) token.getPrincipal();

        //2. 获取用户输入的密码
        String password = new String((char[]) token.getCredentials());

        //3. 对用户输入的密码：加密、加盐
        // 参数1：输入的密码
        // 参数2：盐。 把用户名作为盐
        String encodePwd = new Md5Hash(password,email).toString();

        //4. 数据库中正确的密码
        String dbPassword = (String) info.getCredentials();

        //5. 判断
        return encodePwd.equals(dbPassword);
    }
}
