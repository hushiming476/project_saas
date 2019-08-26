package cn.itcast.web.controller.cargo;


import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.FileUploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *  提供进入货物添加和列表页面的方法。
 */
@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    // 工厂service
    @Reference
    private FactoryService factoryService ;

    // 购销合同货物service
    @Reference
    private ContractProductService contractProductService;

    /**
     * 1. 从购销合同列表，点击货物，进入货物列表和添加页面
     * 请求地址：http://localhost:8080/cargo/contractProduct/list.do?contractId=3
     * 存储数据：工厂、货物、..
     * 响应地址：/WEB-INF/pages/cargo/product/product-list.jsp
     */
    @RequestMapping("/list")
    public String list(String contractId,
                       @RequestParam(defaultValue = "1")Integer pageNum,
                       @RequestParam(defaultValue = "5")Integer pageSize){
        // 1 查询工厂
        FactoryExample factoryExample = new FactoryExample();
        //    查询条件： 工厂类型 = 货物     其他 = 附件
        factoryExample.createCriteria().andCreateByEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        // 2 根据购销合同id，查询货物
        ContractProductExample cpExample  = new ContractProductExample();
        // 查询条件：购销合同id
        cpExample.createCriteria().andContractIdEqualTo(contractId);
        PageInfo<ContractProduct> pageInfo =
                contractProductService.findByPage(cpExample, pageNum, pageSize);

        // 3 保存数据
        request.setAttribute("factoryList",factoryList);
        request.setAttribute("pageInfo",pageInfo);
        request.setAttribute("contractId",contractId);

        return "cargo/product/product-list";
    }

    /**
     *  2.添加/修改货物
     *  请求地址：edit
     *   <input type="file"  name="productPhoto" >
     *  返回地址：cargo/product/product-list
     */

    // 注入上传到七牛云的工具类
    @Autowired
    private FileUploadUtil fileUploadUtil;

    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto){
        // 设置所属部门企业id、名称
        contractProduct.setCompanyId(getLoginCompanyId());
        contractProduct.setCompanyName(getLoginCompanyName());
        // 判断
        if (StringUtils.isEmpty(contractProduct.getId())){
            if (productPhoto !=null){
                try{
                    String url = "http://" + fileUploadUtil.upload(productPhoto);
                    // 保存url
                    contractProduct.setProductImage(url);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            // 添加
            contractProductService.save(contractProduct);
        }else{
            // 修改
            contractProductService.update(contractProduct);

        }
         return "redirect:/cargo/contractProduct/list.do?contractId=" +contractProduct.getContractId();
    }

    /**
     * 3. 进入修改页面
     * 请求地址和参数：http://localhost:8080/cargo/contractProduct/toUpdate.do?id=9
     */

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){

        // 1.根据货物id查询
        ContractProduct contractProduct = contractProductService.findById(id);

        // 2查询货物工厂
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCreateByEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        // 3.保存
        request.setAttribute("contractProduct",contractProduct);
        request.setAttribute("factoryList",factoryExample);

        // 返回
        return "cargo/product/product-update";

    }

    /**
     *  4,删除货物
     *  delete
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId) {
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }

    /**
     * 5. ApachePOI实现货物上传 (1) 进入上传货物页面
     * http://localhost:8080/cargo/contractProduct/toImport.do?contractId=0
     */
    @RequestMapping("/toImport")
    public String toImport(String contractId){
        // 保存购销合同id，因为后面上传货物，要指定对哪个购销合同添加货物
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-import";
    }


    /**
     * 6. ApachePOI实现货物上传 (2) 上传   读取excel--->封装对象--->调用service保存
     * 请求参数：<input type="file" name="file">
     *
     */
    @RequestMapping("/import")
    public String importExcel(String contractId,MultipartFile file) throws Exception {

        // 1. 根据excel文件流，创建工作薄
        Workbook workbook = new XSSFWorkbook(file.getInputStream());

        // 2.获取工作表
        Sheet sheet = workbook.getSheetAt(0);

        // 3. 获取总行数
        int totalRow = sheet.getPhysicalNumberOfRows();

        //4. 遍历 每一行(从第二行开始读取)
        for (int i = 1; i < totalRow; i++) {
            //a. 获取每一行
            Row row = sheet.getRow(i);

         // b,创建货物对象，把excel的每一封装为一个货物对象
            ContractProduct cp = new ContractProduct();
            cp.setContractId(contractId);
            cp.setFactoryName(row.getCell(1).getStringCellValue());
            cp.setProductNo(row.getCell(2).getStringCellValue());
            cp.setCnumber((int) row.getCell(3).getNumericCellValue());
            cp.setPackingUnit(row.getCell(4).getStringCellValue());
            cp.setLoadingRate(row.getCell(5).getNumericCellValue()+"");
            cp.setBoxNum((int) row.getCell(6).getNumericCellValue());
            cp.setPrice(row.getCell(7).getNumericCellValue());
            cp.setProductDesc(row.getCell(8).getStringCellValue());
            cp.setProductRequest(row.getCell(9).getStringCellValue());

            //设置厂家id
            Factory factory = factoryService.findByName(cp.getFactoryName());
            if (factory !=null){
                cp.setFactoryId(factory.getId());
            }
            //保存货物
            contractProductService.save(cp);
        }
        return "cargo/product/product-import";
    }


}
