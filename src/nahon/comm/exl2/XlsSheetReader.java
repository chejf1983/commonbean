/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.exl2;

import java.io.File;
import jxl.Sheet;
import jxl.Workbook;
import static nahon.comm.exl2.XlsConfig.Table_Split;

/**
 *
 * @author chejf
 */
public class XlsSheetReader implements AutoCloseable {

    private Workbook readworkBook = null;
    private Sheet readSheet = null;

    private int p_column = XlsConfig.def_column;//当前指针位置
    private int p_row = XlsConfig.def_row;

    private String file_name;  //文件名称
    private int sheetIndex;//表名称
    private int copy_num = 0;//副本个数

    private XlsSheetReader(String fileName, int sheetIndex) throws Exception {
        this.readworkBook = Workbook.getWorkbook(new File(fileName));
        this.readSheet = readworkBook.getSheet(sheetIndex);

        //保存文件名称
        this.file_name = fileName;
        this.sheetIndex = sheetIndex;

        //设置读指针坐标
        p_column = XlsConfig.def_column;
        p_row = 0;
    }

    public static XlsSheetReader XlsSheetReader(String fileName) throws Exception {
        return ReadExcel(fileName, 0);
    }

    public static XlsSheetReader ReadExcel(String fileName, int sheetIndex) throws Exception {
        return new XlsSheetReader(fileName, sheetIndex);
    }

    // <editor-fold defaultstate="collapsed" desc="Table操作接口"> 
    public xlsTable_R FindeNextTable() throws Exception {
        //找到第一个有效数据
        if (this.ScanLefLine()) {
            return this.ReadTable();
        } else {
            if (this.NewColumn() && this.ScanLefLine()) {
                return this.ReadTable();
            }
        }
        return null;
    }

    private int max_length = 0;

    //再max_row * 3 的区域内，搜索第一个有数据的单元格位置
    private boolean ScanLefLine() {
        int c = 0;

        //先遍历列
        for (; c < 5; c++) {
            int r = 0;
            //后遍历行
            for (; r < XlsConfig.max_row_inc * 2; r++) {
                //判断excel是否为空
                String value = this.readCell(p_column + c, p_row + r);
//                if (value != null && !value.isEmpty()) {
                if (isHead(value)) {
                    //不为空，找到数据，修正启动指针坐标
                    p_row += r;
                    p_column += c;
                    return true;
                }
            }
            p_row = 0;
        }

        return false;
    }

    private boolean isHead(String value) {
        //判断当前是否有值，没值返回空
        if (value == null) {
            return false;
        }
        //检查表头格式   :名字:长:宽:
        if (!(value.startsWith(Table_Split) && value.endsWith(Table_Split))) {
            return false;
        }
        //分解表头内容
        String[] split = value.split(Table_Split);
        if (split.length != 4) {
            return false;
        }
        return true;
    }

    //读取一张表
    private xlsTable_R ReadTable() throws Exception {
        xlsTable_R table = new xlsTable_R();
        //判断当前是否有值，没值返回空
        String value = this.readCell(p_column, p_row);
        if (value == null) {
            throw new Exception("表头:[" + p_row + "," + p_column + "]");
        }
        //检查表头格式   :名字:长:宽:
        if (!(value.startsWith(Table_Split) && value.endsWith(Table_Split))) {
            throw new Exception("无效表头:[" + p_row + "," + p_column + "]" + value);
        }
        //分解表头内容
        String[] split = value.split(Table_Split);
        if (split.length != 4) {
            throw new Exception("无效表头:[" + p_row + "," + p_column + "]" + value);
        }
        //表头为:名称:长:宽:
        table.table_name = split[1];
        int row = Integer.valueOf(split[2]); //row不带名称
        int column = Integer.valueOf(split[3]); //带一列序号
        //读取column名称
        table.column_names = this.ReadLine(column);
        //读取所有行
        for (int i = 0; i < row; i++) {
            String[] line_info = this.ReadLine(column);
            if (line_info != null) {
                table.rows.add(line_info);
//                System.out.println(i + ":" + line_info[0]);
            } else {
                if (this.NewColumn() && this.ScanLefLine()) {
                    line_info = this.ReadLine(column);
                    table.rows.add(line_info);
//                    System.out.println(i + ":" + line_info[0]);
                } else {
                    throw new Exception("数据不完整");
                }
            }
        }

        return table;
    }

    private String readCell(int column, int row) {
        try {
            return readSheet.getCell(column, row).getContents();
        } catch (Exception ex) {
            return null;
        }
    }

    //读取一行数据
    private String[] ReadLine(int length) {
        //判断当前是否有值，没值返回空
        String value = this.readCell(p_column, p_row);
        if (value == null) {
            return null;
        }
        //更新最大宽度

        if (length > this.max_length) {
            this.max_length = length;
        }
        //读取一行数据，第一列不读，默认为序号
        String[] ret = new String[length - 1];
        for (int i = 1; i < length; i++) {
            ret[i - 1] = this.readCell(p_column + i, p_row);
        }
        //行指针自增
        p_row++;
        return ret;
    }

    private boolean NewColumn() throws Exception {
        this.p_row = XlsConfig.def_row;
        if (this.p_column + XlsConfig.max_colum_inc > XlsConfig.MaxExcelWidth) {
            copy_num++;
            this.readworkBook = Workbook.getWorkbook(new File(file_name.replace(".xls", ("_" + copy_num + ".xls"))));
            if (this.readworkBook == null) {
                return false;
            }

            this.readSheet = readworkBook.getSheet(sheetIndex);

            //设置读指针坐标
            p_column = XlsConfig.def_column;
            this.max_length = 0;
            return true;
        } else {
            this.p_column = this.p_column + this.max_length + XlsConfig.min_colum_inc;
            this.max_length = 0;
            return true;
        }
    }
    // </editor-fold>  

    @Override
    public void close() throws Exception {
        if (this.readworkBook != null) {
            this.readworkBook.close();
            this.readworkBook = null;
        }
    }
}
