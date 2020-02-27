/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.file2;

import java.io.IOException;
import static nahon.comm.exl2.XlsConfig.Table_Split;

/**
 *
 * @author chejf
 */
public class fileTable_W {

    int length = 0;
    public final String[] table_columns;
    String table_name;
    private FileWriter2 file_writer;

    public fileTable_W(FileWriter2 file_writer, String t_name, int row_num, String... column_names) {
        this.file_writer = file_writer;
        //初始化名字宽度
        table_columns = new String[column_names.length];
        table_name = Table_Split + t_name + Table_Split + row_num + Table_Split;

        for (int i = 0; i < column_names.length; i++) {
            table_columns[i] = column_names[i];
        }
    }

    public void AddRow(Object... pars) throws Exception {
        String[] spars = new String[pars.length];
        for (int i = 0; i < pars.length; i++) {
            spars[i] = pars[i].toString();
        }
        file_writer.WriteLine(spars);
    }

    public void Finish() throws IOException {
        this.file_writer.FinishTable();
    }
}
