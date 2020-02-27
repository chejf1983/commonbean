/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.file2;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import nahon.comm.exl2.xlsTable_R;

/**
 *
 * @author chejf
 */
public class FileReader2 implements Closeable {

    private FileInputStream in;
    private InputStreamReader inReader;
    private BufferedReader bufReader;

    private FileReader2(File file) throws Exception {
        in = new FileInputStream(file);
        inReader = new InputStreamReader(in, "UTF-8");
        bufReader = new BufferedReader(inReader);
    }

    public static FileReader2 OpenFile(String filename) throws Exception {
        File file = new File(filename);
        if (file.exists()) {
            return new FileReader2(file);
        } else {
            throw new Exception("没有找到文件");
        }
    }

    @Override
    public void close() throws IOException {
        bufReader.close();
        inReader.close();
        in.close();
    }

    public xlsTable_R FindeNextTable() throws Exception {
        String line;
        while ((line = bufReader.readLine()) != null) {
            if (line.startsWith(FileConfig.Table_Split) && line.endsWith(FileConfig.Table_Split)) {
                String[] split = line.split(FileConfig.Table_Split);
                //分解表头内容 :名称:行:
                if (split.length != 3) {
                    throw new Exception("无效表头:" + line);
                }

                //表头为:名称:长:宽:
                String table_name = split[1];
                int row = Integer.valueOf(split[2]); //row不带名称
                return ReadTable(table_name, row);
            }
        }
        return null;
    }

    private xlsTable_R ReadTable(String name, int row) throws Exception {
        xlsTable_R table = new xlsTable_R();
        table.table_name = name;

        //读取列名
        String line = bufReader.readLine();
        table.column_names = line.split(FileConfig.Table_Split);
        
        for(int i = 0; i < row; i++){
            line = bufReader.readLine();
            
            if(line == null){
                throw new Exception("表格长度不完整");
            }
            
            //如果为空，表格长度不足，退出
            if(line.contentEquals(""))
                break;
            
            table.rows.add(line.split(FileConfig.Table_Split));
        }
        
        return table;
    }
}
