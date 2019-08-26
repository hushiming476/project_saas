package cn.itcast.web.controller.stat;

import cn.itcast.service.stat.StatService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/stat")
public class StatController extends BaseController {

    @Reference
    private StatService statService;

    /**
     *  生产厂家销售情况 toCharts
     */
    @RequestMapping("/toCharts")
    public String toCharts(String chartsType){
        return "stat/stat-" + chartsType;
    }

    /**
     * 需求1 ：生产厂家销售情况
     */
    @RequestMapping("/getFactorySale")
    @ResponseBody    // 自动把方法返回对象转换为json格式
    public List<Map<String,Object>> getFactorySale(){
        List<Map<String, Object>> factorySale = statService.getFactorySale(getLoginCompanyId());
        return factorySale;
    }

    /**
     *报表统计需求2：产品销售排行，前5
     */
    @RequestMapping("/getProductSale")
    @ResponseBody    // 自动把方法返回对象转换为json格式
    public List<Map<String,Object>> getProductSale(){
        List<Map<String, Object>> factorySale = statService.getProductSale(getLoginCompanyId(),5);
        return factorySale;
    }

    /**
     * 需求3 ：系统访问压力图,按小时统计访问人数
     */
    @RequestMapping("/getOnline")
    @ResponseBody    // 自动把方法返回对象转换为json格式
    public List<Map<String,Object>> getOnline(){
        List<Map<String, Object>> factorySale = statService.getOnline();
        return factorySale;
    }
}
