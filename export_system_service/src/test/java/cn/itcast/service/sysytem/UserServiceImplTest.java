package cn.itcast.service.sysytem;


import cn.itcast.service.system.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void findByPage(){
        System.out.println(userService.findByPage("1",1,2));
    }
}
