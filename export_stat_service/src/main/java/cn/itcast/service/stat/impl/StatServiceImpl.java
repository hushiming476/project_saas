package cn.itcast.service.stat.impl;

import cn.itcast.dao.stat.StatDao;
import cn.itcast.service.stat.StatService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class StatServiceImpl implements StatService{

    @Autowired
    private StatDao statDao;

    @Override
    public List<Map<String, Object>> getFactorySale(String companyId) {
        return statDao.getFactorySale(companyId);
    }

    @Override
    public List<Map<String, Object>> getProductSale(String companyId, int top) {
        return statDao.getProductSale(companyId,top);
    }

    @Override
    public List<Map<String, Object>> getOnline() {
        return statDao.getOnline();
    }
}
