package cn.itcast.web.shiro;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
   自定义凭证匹配器：加盐加密--还要在applicationContext-shiro.xml中配置

 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        //1.获取用户输入的用户名
        String email = (String) token.getPrincipal();

        //2.获取用户输入的密码
        String password = new String((char[])token.getCredentials());

        //3.对用户输入的密码：加密。加盐
        //参数一：输入的密码
        //参数二：盐、把用户名作为盐
        String encodePwd = new Md5Hash(password,email).toString();

        //4.数据库中正确的密码
        String dbPassword = (String) info.getCredentials();

        //5.判断
        return encodePwd.equals(dbPassword);
    }
}
