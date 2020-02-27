/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.exl2;

import java.io.File;
import java.util.ArrayList;
import jxl.Workbook;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author chejf
 */
public class XlsSheetWriter implements AutoCloseable {

    // <editor-fold defaultstate="collapsed" desc="Sheet接口"> 
    private WritableWorkbook workbook;
    private WritableSheet sheet;

    private int p_column = XlsConfig.def_column;//当前指针位置
    private int p_row = XlsConfig.def_row;
    private ArrayList<Integer> p_column_width = new ArrayList();

    private String file_name;  //文件名称
    private String sheet_name;//表名称
    private int copy_num = 0;//副本个数
    //初始化一个sheet

    //创建一个Sheet写入器
    public static XlsSheetWriter CreateSheet(String file_name, String sheet_name) throws Exception {
        return new XlsSheetWriter(file_name, sheet_name);
    }

    private XlsSheetWriter(String file_name, String sheet_name) throws Exception {
        //创建excel
        this.workbook = Workbook.createWorkbook(new File(file_name));
        //创建新的sheet
        this.sheet = workbook.createSheet(sheet_name, 0);

        //保存文件名称
        this.file_name = file_name;
        this.sheet_name = sheet_name;

        //设置写指针坐标
        p_column = XlsConfig.def_column;
        p_row = 0;//第一张表格会自动+1，所以从0行开始
    }

    @Override
    public void close() throws Exception {
        if (this.workbook != null) {
            //关闭文件前调整列宽
            this.AdjustColumnWidth();
            workbook.write();
            workbook.close();
            this.workbook = null;
            this.sheet = null;
        }
    }

    //新建一列
    private void NewColumn() throws Exception {
        //列剩余空间不足，重新建里新xls文件
        if (this.p_column + XlsConfig.max_colum_inc > XlsConfig.MaxExcelWidth) {
            //关闭当前xls文件
            this.close();
            //副本数增加
            this.copy_num++;
            //创建excel副本
            this.workbook = Workbook.createWorkbook(new File(file_name.replace(".xls", ("_" + copy_num + ".xls"))));
            //创建新的sheet
            this.sheet = workbook.createSheet(sheet_name, 0);

            //指针归为
            p_column = XlsConfig.def_column;
            p_row = XlsConfig.def_row;
        } else {
            //剩余空间足够，创建新的列
            int last_column_width = this.p_column_width.size();
            //调整上一列的列宽
            this.AdjustColumnWidth();

            //水平增长，column指针增加
            p_column += last_column_width + XlsConfig.min_colum_inc;
            //行指针恢复
            p_row = XlsConfig.def_row;
        }
    }

    //调整表格宽度
    private void AdjustColumnWidth() {
        //调整单元最大宽度
        for (int i = 0; i < this.p_column_width.size(); i++) {
            //更新每个column的最大宽度
            sheet.setColumnView(p_column + i, this.p_column_width.get(i) + 3);
        }
        //调整完毕，清除数据
        this.p_column_width.clear();
    }
    // </editor-fold>   

    // <editor-fold defaultstate="collapsed" desc="Table操作接口"> 
    private xlsTable_W last_table = null;

    public enum DirecTion {
        Vertical,
        Horizontal
    }

    public xlsTable_W CreateNewTable(String name, long row_num, String... pars) throws Exception {
        return this.CreateNewTable(name, row_num, DirecTion.Vertical, pars);
    }

    //创建新表
    public xlsTable_W CreateNewTable(String name, long row_num, DirecTion dir, String... pars) throws Exception {
        if (last_table != null) {
            throw new Exception("Excel 正在写入...");
        }

        if (dir == DirecTion.Vertical) {
            //垂直增长,
            if (p_row + XlsConfig.max_row_inc > XlsConfig.MaxExcelLen) {
                //如果长度不够，新建Column
                this.NewColumn();
            } else {
                //如果长度够，继续增长
                p_row += +XlsConfig.min_row_inc;
            }
        } else {
            //水平增长，新建column
            this.NewColumn();
        }
        //创建新表
        last_table = new xlsTable_W(this, p_column, p_row, name, row_num, pars);

        //填写表名称
        this.AddLine(last_table.table_names);
        return last_table;
    }

    //添加一行数据
    void AddLine(Object[] pars) throws Exception {
        //超过最长值后，水平平移
        if (this.p_row > XlsConfig.MaxExcelLen) {
            //刷新宽度
            this.NewColumn();
        }

        //填写每行的数据
        for (int column = 0; column < pars.length; column++) {
            //填入内容
            Object value = pars[column];
            WriteCell(p_column + column, p_row, value);

            //更新最大column宽度个数
            if (this.p_column_width.size() < last_table.table_names.length) {
                this.p_column_width.add(0);
            }

            //更新最大column宽度
            if (this.p_column_width.get(column) < pars[column].toString().length()) {
                this.p_column_width.set(column, pars[column].toString().length());
            }
        }
        //行增加
        p_row++;
    }

    //关闭当前表
    void FinishTabel() {
        //清除表
        this.last_table = null;
    }

    void WriteCell(int column, int row, Object value) throws Exception {
        //创建cell 格式
        WritableCellFormat wcf = new WritableCellFormat();
        wcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        wcf.setAlignment(jxl.format.Alignment.CENTRE);

        //内容为空，填空值
        if (value == null) {
            jxl.write.Label data = new jxl.write.Label(column, row, "null", wcf);
            this.sheet.addCell(data);
        } else if (java.lang.Number.class.isAssignableFrom(value.getClass())) {
            //判断是数字类型
            Double dvalue = Double.valueOf(value.toString());
            if (Double.isNaN(dvalue)) {
                jxl.write.Label data = new jxl.write.Label(column, row, "NaN", wcf);
                this.sheet.addCell(data);
            } else {
                jxl.write.Number data = new jxl.write.Number(column, row, dvalue, wcf);
                this.sheet.addCell(data);
            }
        } else {
            //判断是文字类型
            jxl.write.Label data = new jxl.write.Label(column, row, value.toString(), wcf);
            this.sheet.addCell(data);
        }
    }
    // </editor-fold>   

    public static void main(String... args) throws Exception {
        XlsConfig.MaxExcelLen = 60;
        XlsConfig.MaxExcelWidth = 10;

        try (XlsSheetWriter CreateSheet = XlsSheetWriter.CreateSheet("./test.xls", "TestData")) {
            for (int i = 0; i < 10; i++) {
                int data_len = 30;
                xlsTable_W CreateNewTable;
                if (i % 2 == 0) {
                    CreateNewTable = CreateSheet.CreateNewTable("table" + i, data_len, "value");
                }else
                    CreateNewTable = CreateSheet.CreateNewTable("table" + i, data_len, DirecTion.Horizontal, "value");
                
                for (int j = 0; j < data_len; j++) {
                    CreateNewTable.WriterLine("data" + j);
                }
                CreateNewTable.Finish();
            }
        }
        XlsSheetReader ReadExcel = XlsSheetReader.ReadExcel("./test.xls", 0);

        for (int i = 0; i < 10; i++) {
            xlsTable_R table = ReadExcel.FindeNextTable();
            if (table != null) {
                System.out.println(table.table_name + ":" + table.rows.size());
            }
        }

    }
}
