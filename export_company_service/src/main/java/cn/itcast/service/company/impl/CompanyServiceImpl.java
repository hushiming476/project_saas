package cn.itcast.service.company.impl;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

// import com.alibaba.dubbo.config.annotation.Service;
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    public List<Company> findAll() {
        return companyDao.findAll();
    }

    @Override
    public void save(Company company) {
        //设置企业id
        company.setId(UUID.randomUUID().toString());
        companyDao.save(company);
    }

    @Override
    public void update(Company company) {
        companyDao.update(company);
    }

    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    @Override
    public void delete(String id) {
        companyDao.delete(id);
    }

    @Override
    public PageInfo<Company> findByPage(int pageNum, int pageSize) {
        // 开始分页, PageHelper组件会自动对其后的第一条查询查询分页
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao查询
        List<Company> list = companyDao.findAll();
        // 创建PageInfo对象封装分页结果，传入查询集合。会自动计算分页参数
        PageInfo<Company> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}