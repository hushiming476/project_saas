package cn.itcast.dao.system;


import cn.itcast.domain.system.Dept;

import java.util.List;

public interface DeptDao {

    List<Dept> findAll(String companyId);

    Dept findById(String id);

    void save(Dept dept);

    void update(Dept dept);

    boolean delete(String id);
}
