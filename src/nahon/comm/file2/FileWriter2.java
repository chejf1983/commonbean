/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.file2;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author chejf
 */
public class FileWriter2 implements Closeable {

    private FileOutputStream out;
    private OutputStreamWriter outWriter;
    private BufferedWriter bufWrite;

    private FileWriter2(File savefile) throws Exception {
        out = new FileOutputStream(savefile);
        outWriter = new OutputStreamWriter(out, "UTF-8");
        bufWrite = new BufferedWriter(outWriter);
    }

    public static FileWriter2 OpenFile(String filename) throws Exception {
        File file = new File(filename);
        return new FileWriter2(file);
    }

    // <editor-fold defaultstate="collapsed" desc="Table操作接口"> 
    private fileTable_W lastfile;

    public fileTable_W CreateTable(String table_name, int num, String... columns) throws Exception {
        if (lastfile != null) {
            throw new Exception("文件正在写");
        }

        //创建新表
        lastfile = new fileTable_W(this, table_name, num, columns);
        //填写表名
        bufWrite.write(lastfile.table_name + "\r\n");
        this.WriteLine(lastfile.table_columns);
        return lastfile;
    }

    void WriteLine(String... args) throws Exception {
        //添加结束符号
        String line = "";
        for (String arg : args) {
            line += (arg + FileConfig.Table_Split);
        }
        line += "\r\n";
        bufWrite.write(line);
    }

    void FinishTable() throws IOException {
        if (lastfile != null) {
            //空一行
            bufWrite.write("\r\n");
            lastfile = null;
        }
    }
    // </editor-fold>   

    @Override
    public void close() throws IOException {
        this.FinishTable();
        bufWrite.close();
        outWriter.close();
        out.close();
    }
}
