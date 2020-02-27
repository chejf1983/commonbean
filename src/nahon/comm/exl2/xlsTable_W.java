/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.exl2;

import static nahon.comm.exl2.XlsConfig.Table_Split;

/**
 *
 * @author chejf
 */
public class xlsTable_W {

    public int start_c = 0;
    public int start_r = 0;
    int length = 0;
    public final String table_names[];
    private final XlsSheetWriter sheet_wirter;

    xlsTable_W(XlsSheetWriter writer, int start_c, int start_r, String t_name, long row_num, String... column_names) throws Exception {
        //初始化起点坐标
        this.start_c = start_c;
        this.start_r = start_r;
        //初始化sheet_wirter
        this.sheet_wirter = writer;
        //初始化名字宽度,宽度加上表名称
        table_names = new String[1 + column_names.length];
        table_names[0] = Table_Split + t_name + Table_Split + row_num + Table_Split + table_names.length + Table_Split;
        for (int i = 0; i < column_names.length; i++) {
            table_names[i + 1] = column_names[i];
        }
    }
    
    //添加行
    public void WriterLine(Object... values) throws Exception {
        //检查表头和内容宽度是否一致
        if (values.length + 1 != this.table_names.length) {
            throw new Exception("表格宽度与名称不一致");
        }

        length++;
        Object[] pars = new Object[values.length + 1];
        pars[0] = length;
        System.arraycopy(values, 0, pars, 1, values.length);
        this.sheet_wirter.AddLine(pars);
    }

    //结束表格操作
    public void Finish() throws Exception {
        this.sheet_wirter.FinishTabel();
    }
}
