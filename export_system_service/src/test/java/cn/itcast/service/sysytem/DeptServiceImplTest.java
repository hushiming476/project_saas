package cn.itcast.service.sysytem;


import cn.itcast.service.system.DeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//classpath*:所有目录
//classpath:当前目录
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")
public class DeptServiceImplTest {

    @Autowired
    private DeptService deptService;

    /**
      漏了根据部门ID查询的SQL语句的映射会出错：findById
      分页查询：findByPage
      根据企业的主键ID查询：findAll
     */
    @Test
    public void findByPage(){
        System.out.println(deptService.findByPage("1",1,2));

    }

}
