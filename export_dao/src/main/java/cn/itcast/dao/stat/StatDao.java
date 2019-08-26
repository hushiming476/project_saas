package cn.itcast.dao.stat;

import java.util.List;
import java.util.Map;

public interface StatDao {

    /**
     * 需求一 ：生产厂家销售情况
     */

    List<Map<String,Object>> getFactorySale(String companyId);

    List<Map<String,Object>> getProductSale(String companyId, int top);

    List<Map<String,Object>> getOnline();
}
