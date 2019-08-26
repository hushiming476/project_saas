package cn.itcast.service.company;

import cn.itcast.domain.company.Company;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CompanyService {
    /**
     * 查询所有企业
     */
    List<Company> findAll();

    /**
     * 分页查询所有企业
     * @param pageNum  当前页
     * @param pageSize 页大小
     * @return 返回PageHelper提供的封装分页参数的对象
     */
    PageInfo<Company> findByPage(int pageNum, int pageSize);

    /**
     * 添加
     * @param company
     */
    void save(Company company);

    /**
     * 修改
     * @param company
     */
    void update(Company company);

    /**
     * 根据id查询
     * @param id 企业id
     * @return 返回企业对象
     */
    Company findById(String id);

    /**
     * 删除企业
     * @param id 根据主键删除
     */
    void delete(String id);
}
