package cn.itcast.web.controller.cargo;


import cn.itcast.domain.vo.ContractProductVo;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.DownloadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 出货表
 * http://localhost:8088/cargo/contract/print.do
 */
@Controller
@RequestMapping("/cargo/contract")
public class OutProductController extends BaseController {

    // 注入货物
    @Reference
    private ContractProductService contractProductService;

    /**
     *  进入出货表页面
     */
    @RequestMapping("/print")
    public String print() {
        return "cargo/print/contract-print";

    }

    /**
     * 导出出货表（2）导出、下载  SXSSF 导出百万数据
     * http://localhost:8080/cargo/contract/print.do
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws IOException {
        // 导出第一行

        // 创建工作薄
        Workbook workbook = new SXSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet("导出出货表");
        // 设置列宽
        sheet.setColumnWidth(0, 256 * 5);
        sheet.setColumnWidth(1, 256 * 15);
        sheet.setColumnWidth(2, 256 * 26);
        sheet.setColumnWidth(3, 256 * 15);
        sheet.setColumnWidth(4, 256 * 29);
        sheet.setColumnWidth(5, 256 * 15);
        sheet.setColumnWidth(6, 256 * 15);
        sheet.setColumnWidth(7, 256 * 15);
        sheet.setColumnWidth(8, 256 * 15);

        // 合并单元格  开始行0 结束行0 开始列1  结束列8
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));

        // 创建第一行
        Row row = sheet.createRow(0);
        // 设置行高
        row.setHeightInPoints(36);
        // 创建第一行第二列
        Cell cell = row.createCell(1);
        // 设置单元格内容 ： 2019-06 ---> 2019年6月份出货表
        String result = inputDate.replaceAll("-0","-").replaceAll("-","年") + "月份出货表";
        cell.setCellValue(result);

        // 设置单元格样式
        cell.setCellStyle(this.bigTitle(workbook));

        // 第二步：导出第二行
        String[] titles = {"客户", "订单号", "货号", "数量", "工厂", "工厂交期", "船期", "贸易条款"};
        row = sheet.createRow(1);
        row.setHeightInPoints(26);

        //创建第二行的每一列
        for (int i = 0; i < titles.length; i++) {
            cell = row.createCell(i+1);
            //设置列内容
            cell.setCellValue(titles[i]);
            // 设置列样式
            cell.setCellStyle(this.title(workbook));

        }

        // 第三步 ：导出数据行，从第三行开始
        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(), inputDate);
        if (list !=null && list.size()>0){
            int num = 2;
            for (ContractProductVo cp : list) {
                for (int i = 0; i <10000; i++) {
                    row = sheet.createRow(num++);

                    cell = row.createCell(1);
                    cell.setCellValue(cp.getCustomName());

                    cell = row.createCell(2);
                    cell.setCellValue(cp.getContractNo());

                    cell = row.createCell(3);
                    cell.setCellValue(cp.getProductNo());


                    cell = row.createCell(4);
                    if (cp.getCnumber() == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(cp.getCnumber());
                    }

                    cell = row.createCell(5);
                    cell.setCellValue(cp.getFactoryName());

                    cell = row.createCell(6);
                    cell.setCellValue(cp.getDeliveryPeriod());

                    cell = row.createCell(7);
                    cell.setCellValue(cp.getShipTime());

                    cell = row.createCell(8);
                    cell.setCellValue(cp.getTradeTerms());

                }
            }

        }
        // 第四步：导出下载
        DownloadUtil downloadUtil = new DownloadUtil();
        // 缓冲流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // excel文件流 ---> 缓冲流
        workbook.write(bos);
        // 下载：缓冲流 --->response输出流
        downloadUtil.download(bos,response,"出货表.xlsx");
        workbook.close();


    }
    //大标题的样式
    public CellStyle bigTitle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);                //横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线

        return style;
    }
}
