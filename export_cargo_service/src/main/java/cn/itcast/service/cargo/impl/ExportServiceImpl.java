package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.*;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExportService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 出口报运单业务实现
 */
@Service
public class ExportServiceImpl implements ExportService {

    // 注入dao
    @Autowired
    private ExportDao exportDao;
    // 注入货物dao
    @Autowired
    private ContractProductDao contractProductDao;
    // 注入附件dao
    @Autowired
    private ExtCproductDao extCproductDao;
    // 注入购销合同dao
    @Autowired
    private ContractDao contractDao;
    // 注入商品dao
    @Autowired
    private ExportProductDao exportProductDao;
    // 注入商品附件dao
    @Autowired
    private ExtEproductDao extEproductDao;

    @Override
    public PageInfo<Export> findByPage(ExportExample exportExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Export> list = exportDao.selectByExample(exportExample);
        return new PageInfo<>(list);
    }

    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    // 生成报运单
    @Override
    public void save(Export export) {
        //1. 设置报运单id
        export.setId(UUID.randomUUID().toString());
        // 设置制单时间
        export.setInputDate(new Date());
        // 获取多个购销合同id数组
        String[] array = export.getContractIds().split(",");
        // 设置合同号
        String contractNos = "";
        for (String contractId : array) {
            // 根据购销合同id查询
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            // 获取合同号 (多个合同号以空格隔开)
            contractNos += contract.getContractNo() + " ";

            // 修改购销合同状态为2
            contract.setState(2);
            contractDao.updateByPrimaryKeySelective(contract);
        }
        // 设置合同号
        export.setCustomerContract(contractNos);


        //2. 保存报运的商品
        //需求： 报运的商品数据来源：购销合同的货物
        // select * from co_contract_product where contract_id in ()

        /**
         * 定义一个map集合，存储货物id、商品id
         * Map<货物id，商品id>  map
         */
        Map<String,String> map = new HashMap<>();

        //2.1 根据购销合同id，查询货物
        ContractProductExample cpExample = new ContractProductExample();
        cpExample.createCriteria().andContractIdIn(Arrays.asList(array));
        List<ContractProduct> cpList = contractProductDao.selectByExample(cpExample);
        //2.2 遍历货物, 构造报运的商品
        for (ContractProduct contractProduct : cpList) {//一个货物，一个商品
            //A. 创建商品对象
            ExportProduct exportProduct = new ExportProduct();
            //B. 货物--->商品。  import org.springframework.beans.BeanUtils;
            BeanUtils.copyProperties(contractProduct,exportProduct);
            //C. 设置商品属性
            exportProduct.setId(UUID.randomUUID().toString());
            exportProduct.setExportId(export.getId());
            //D. 保存商品
            exportProductDao.insertSelective(exportProduct);

            // 存储货物id，以及对应的商品id
            map.put(contractProduct.getId(),exportProduct.getId());
        }

        //3. 保存报运的商品附件  (关键点：报运单id，每一个报运的商品id)
        // 需求： 报运的商品附件数据来源：购销合同的附件
        // select * from co_ext_cproduct where contract_id in (..)
        //3.1 根据购销合同id，查询附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria()
                .andContractIdIn(Arrays.asList(array));
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(extCproductExample);
        //3.2 遍历购销合同附件，作为报运单商品附件
        for (ExtCproduct extCproduct : extCproductList) {
            //A. 创建商品附件
            ExtEproduct extEproduct = new ExtEproduct();
            //B. 货物附件 ----> 商品附件
            BeanUtils.copyProperties(extCproduct,extEproduct);
            //C. 设置商品附件属性
            extEproduct.setId(UUID.randomUUID().toString());
            // 设置报运单id
            extEproduct.setExportId(export.getId());
            /**
             * 设置商品id
             * 已知条件： 货物id
             *          extCproduct.getContractProductId()
             * 求：      上一步保存的报运单的商品id
             */
            extEproduct.setExportProductId(map.get(extCproduct.getContractProductId()));
            //D. 保存商品附件
            extEproductDao.insertSelective(extEproduct);
        }


        //4. 保存报运单
        //4.1 设置报运单状态
        export.setState(0);
        //4.2 设置商品数、附件数
        export.setProNum(cpList.size());
        export.setExtNum(extCproductList.size());
        //4.3 保存报运单
        exportDao.insertSelective(export);
    }

    @Override
    public void update(Export export) {
        //1. 修改报运单
        exportDao.updateByPrimaryKeySelective(export);

        //2. 修改报运的商品
        //2.1 获取报运单的商品集合
        List<ExportProduct> epList = export.getExportProducts();
        //2.1 遍历
        if (epList != null && epList.size()>0){
            for (ExportProduct exportProduct : epList) {
                //2.2 修改商品
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    @Override
    public void delete(String id) {
        exportDao.deleteByPrimaryKey(id);
    }
}
