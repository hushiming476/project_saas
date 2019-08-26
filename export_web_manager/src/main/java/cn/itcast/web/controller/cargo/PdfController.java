package cn.itcast.web.controller.cargo;


import cn.itcast.domain.cargo.Export;
import cn.itcast.domain.cargo.ExportProduct;
import cn.itcast.domain.cargo.ExportProductExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.BeanMapUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;
import java.io.InputStream;
import java.util.*;

/**
 * PDF报表
 */
@Controller
@RequestMapping("/cargo/export")
public class PdfController extends BaseController {

    /**
     * 导出PDF（1）简单测试
     * http://localhost:8080/cargo/export/exportPdf.do
     */
    @RequestMapping("/exportPdf1")
    public void exportPdf1() throws Exception {
        // 加载jasper文件流
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test02.jasper");

        /**
         * 创建JasperPrint对象
         * 参数一：jasper文件流
         * 参数二：往模板中填充map数据
         * 参数三：往模板中填充数据源数据
         *
         */
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in,new HashMap<>(),new JREmptyDataSource());

        // 导出PDF
        JasperExportManager
                .exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }

    /**
     * 导出PDF（2）简单测试 + 中文字体
     * http://localhost:8080/cargo/export/exportPdf.do
     */
    @RequestMapping("/exportPdf2")
    public void exportPdf2() throws Exception {
        // 加载jasper文件流
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test03.jasper");

        // 构建map数据，map的key与模板文件中的Parameter参数名称一致
        Map<String,Object> map = new HashMap<>();
        map.put("username","肉肉");
        map.put("deptName","影视部");
        map.put("companyName","video");
        map.put("email","www@baidu.com");

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in,map,new JREmptyDataSource());

        // 导出PDF
        JasperExportManager
                .exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }

    /**
     * 导出PDF(3)jdbc数据源填充数据
     * http://localhost:8080/cargo/export/exportPdf.do
     */
    @Autowired
    private DataSource dataSource;
    @RequestMapping("/exportPdf3")
    public void exportPdf3() throws Exception {
        // 加载jasper文件流
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test04.jasper");


        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in,new HashMap<>(),dataSource.getConnection());

        // 导出PDF
        JasperExportManager
                .exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }

    // （五）JavaBean数据源
    @RequestMapping("/exportPdf4")
    public void exportPdf4() throws Exception {
        // 加载jasper文件流
        InputStream in =
                session.getServletContext()
                        .getResourceAsStream("/jasper/test05.jasper");

        // 集合数据
        List<User> list = new ArrayList<>();
        for (int i = 1; i <=5; i++) {
            User user = new User();
            user.setUserName("老胡");
            user.setCompanyName("竞技");
            user.setEmail("3699@qq.com");
            user.setDeptName("游戏部");
            list.add(user);
        }
        // 构造数据源
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in,new HashMap<>(),dataSource);

        // 导出PDF
        JasperExportManager
                .exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }


    // （五）JavaBean数据源
    @RequestMapping("/exportPdf5")
    public void exportPdf5() throws Exception {
        // 加载jasper文件流
        InputStream in =
                session.getServletContext()
                        .getResourceAsStream("/jasper/test06.jasper");

        // 集合数据
        List<User> list = new ArrayList<>();
        for (int j = 1; j<=3; j++) {
        for (int i = 1; i <=5; i++) {
            User user = new User();
            user.setUserName("老胡"+i);
            user.setCompanyName("竞技"+j);
            user.setEmail("3699@qq.com");
            user.setDeptName("游戏部");
            list.add(user);
          }
        }

        // 构造数据源
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in,new HashMap<>(),dataSource);

        // 导出PDF
        JasperExportManager
                .exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }

    // 数据填充（七）图形报表
    @RequestMapping("/exportPdf6")
    public void exportPdf6() throws Exception {
        // 加载jasper文件流
        InputStream in =
                session.getServletContext().getResourceAsStream("/jasper/test07.jasper");

        // 集合数据
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 1; i <=5; i++) {
           Map<String,Object> map = new HashMap<>();
           map.put("title","月饼" +i);
           map.put("value",new Random().nextInt(100));
           list.add(map);
        }

        // 构造数据源
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        JasperPrint jasperPrint = JasperFillManager.fillReport(in,new HashMap<>(),dataSource);

        // 导出PDF
        JasperExportManager
                .exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }


    // 报运详情PDF报表
    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;

    @RequestMapping("/exportPdf")
    @ResponseBody     // 不进行跳转
    public void exportPdf(String id) throws Exception {
        // 加载jasper文件流
        InputStream in =
                session.getServletContext().getResourceAsStream("/jasper/export.jasper");

        // 构造map 数据（报运单）
        Export export = exportService.findById(id);
        Map<String,Object> map = BeanMapUtils.beanToMap(export);

        // 构造list数据（商品）
        ExportProductExample epExmap = new ExportProductExample();
        epExmap.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> list = exportProductService.findAll(epExmap);

        // 构造数据源
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        // 创建JasperPrint对象，填充map、list数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(in,map,dataSource);

        // 导出PDF
        response.setContentType("application/pdf;charset=utf-8");
        response.setHeader("context-disposition","attachment;fileName-export.pdf");
       ServletOutputStream out = response.getOutputStream();

        JasperExportManager
                .exportReportToPdfStream(jasperPrint,out);
    }

}
