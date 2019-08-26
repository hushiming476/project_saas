package cn.itcast.service.stat;

import java.util.List;
import java.util.Map;

public interface StatService {
    /**
     * 需求1：根据生产厂家统计货物销售金额
     */
    List<Map<String,Object>> getFactorySale(String companyId);

    /**
     * 需求2：产品销售排行，前5
     */
    List<Map<String,Object>> getProductSale(String companyId,int top);

    /**
     * 需求3：按小时统计访问人数
     */
    List<Map<String,Object>> getOnline();
}
