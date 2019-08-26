package cn.itcast.company;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

public class Realm_Md5_Test {

    @Test
    public void md5(){
        System.out.println(new Md5Hash("1").toString());
    }

    // 加密加盐
    @Test
    public void md5Salt(){
        // 用户名
      // String username = "lw@export.com";
 //       String username = "xiaoer@export.com";  zbz@export.com
        String username = "zbz@export.com";
        // 密码
        String password = "1";
        // 参数1：密码, 参数2：盐；把用户名作为盐
        Md5Hash encodePassword = new Md5Hash(password, username);
        // e1087d424b213621545713b872420c7b
        System.out.println("根据用户名作为盐，加密加盐后的结果：" + encodePassword);
    }
}
