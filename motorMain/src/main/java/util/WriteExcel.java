package util;

import java.io.*;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcel {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    public static void main(String[] args) {

//        Map<String, String> dataMap=new HashMap<String, String>();
//        dataMap.put("BankName", "BankName");
//        dataMap.put("Addr", "Addr");
//        dataMap.put("Phone", "Phone");
//        List<Map> list=new ArrayList<Map>();
//        list.add(dataMap);
//        writeExcel(list, 3, "D:/writeExcel.xlsx");
        Map<String, List<KaoqingExcel>> stringListMap = readExcel("D:/201805.xlsx");
        writeExcel(stringListMap,"D:/hello.xlsx");
    }

    public static Map<String,List<KaoqingExcel>> readExcel(String path){
        Map<String,List<KaoqingExcel>> kaoqingExcelMap=new HashMap<>();
        File file = new File(path);
        if(!file.exists()){
            System.out.println("文件不存在");
            return null;
        }
        try {
            Workbook workbok = getWorkbok(file);
            Sheet sheet = workbok.getSheetAt(0);
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 0; i < lastRowNum; i++) {
                Row row = sheet.getRow(i);
                String name = row.getCell(1).toString();
                List<KaoqingExcel> kaoqingExcelList = kaoqingExcelMap.get(name);
                if(kaoqingExcelList==null){
                    kaoqingExcelList=new ArrayList<>();
                    kaoqingExcelMap.put(name,kaoqingExcelList);
                }
                String dateTime = row.getCell(3).toString();
                kaoqingExcelList.add(new KaoqingExcel(name,dateTime));
                System.out.println(name+"  "+dateTime);
            }
            return kaoqingExcelMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kaoqingExcelMap;
    }

//    private static List<> changeData(){
//
//    }

    public static void writeExcel(Map<String, List<KaoqingExcel>> dataList,String finalXlsxPath){
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //新建工作表
        XSSFSheet sheet = workbook.createSheet("考勤");
        Iterator<Map.Entry<String, List<KaoqingExcel>>> iterator = dataList.entrySet().iterator();
        int rownum=0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018,5,0);
        int dayNum = calendar.get(Calendar.DAY_OF_MONTH);
        int[] numList=new int[dayNum];
        while (iterator.hasNext()){
            Map.Entry<String, List<KaoqingExcel>> next = iterator.next();
            String name = next.getKey();
            List<KaoqingExcel> kaoqingExcelList = next.getValue();
            for (int i = 0; i < kaoqingExcelList.size(); i++) {
                rownum+=i;
                KaoqingExcel kaoqingExcel = kaoqingExcelList.get(i);
                String dateTime = kaoqingExcel.getDateTime();
                String[] split1 = dateTime.split("/");
                int _day = Integer.valueOf(split1[2]);
                String[] split = dateTime.split(" ");
                String date = split[0];
                if(_day==i){

                }
                String time = split[1];
                numList[i*2]=1;
                numList[i*2+1]=2;
                //创建行
                XSSFRow row = sheet.createRow(rownum);

                //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
//                for (int j = 0; j < 32; j++) {
//                    XSSFCell cell = row.createCell(j);
//                    //给单元格赋值
//                    cell.setCellValue(kaoqingExcel.getName());
//                }

            }
        }

        //创建输出流
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(finalXlsxPath));
            workbook.write(fos);
            workbook.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeExcel(List<Map> dataList, int cloumnCount,String finalXlsxPath){
        OutputStream out = null;
        try {
            // 获取总列数
            int columnNumCount = cloumnCount;
            // 读取Excel文档
            File finalXlsxFile = new File(finalXlsxPath);
            if(!finalXlsxFile.exists()){
                boolean newFile = finalXlsxFile.createNewFile();
                System.out.println(""+newFile);
                finalXlsxFile = new File(finalXlsxPath);
            }
            Workbook workBook = getWorkbok(finalXlsxFile);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);
            /**
             * 删除原有数据，除了属性列
             */
            int rowNumber = sheet.getLastRowNum();    // 第一行从0开始算
            System.out.println("原始数据总行数，除属性列：" + rowNumber);
            for (int i = 1; i <= rowNumber; i++) {
                Row row = sheet.getRow(i);
                sheet.removeRow(row);
            }
            // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out =  new FileOutputStream(finalXlsxPath);
            workBook.write(out);
            /**
             * 往Excel中写新数据
             */
            for (int j = 0; j < dataList.size(); j++) {
                // 创建一行：从第二行开始，跳过属性列
                Row row = sheet.createRow(j + 1);
                // 得到要插入的每一条记录
                Map dataMap = dataList.get(j);
                String name = dataMap.get("BankName").toString();
                String address = dataMap.get("Addr").toString();
                String phone = dataMap.get("Phone").toString();
                for (int k = 0; k <= columnNumCount; k++) {
                    // 在一行内循环
                    Cell first = row.createCell(0);
                    first.setCellValue(name);

                    Cell second = row.createCell(1);
                    second.setCellValue(address);

                    Cell third = row.createCell(2);
                    third.setCellValue(phone);
                }
            }
            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out =  new FileOutputStream(finalXlsxPath);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("数据导出成功");
    }

    /**
     * 判断Excel的版本,获取Workbook
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbok(File file) throws IOException{
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if(file.getName().endsWith(EXCEL_XLS)){     //Excel&nbsp;2003
            wb = new HSSFWorkbook(in);
        }else if(file.getName().endsWith(EXCEL_XLSX)){    // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }
}