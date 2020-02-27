/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.save;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author chejf
 */
public class FileReader implements Closeable {

    FileInputStream in;
    InputStreamReader inReader;
    BufferedReader bufReader;

    private FileReader(File file) throws Exception {
        in = new FileInputStream(file);
        inReader = new InputStreamReader(in, "UTF-8");
        bufReader = new BufferedReader(inReader);
    }

    public static FileReader OpenFile(String filename) throws Exception {
        File file = new File(filename);
        if (file.exists()) {
            return new FileReader(file);
        } else {
            throw new Exception("没有找到配置文件");
        }
    }

    @Override
    public void close() throws IOException {
        bufReader.close();
        inReader.close();
        in.close();
    }

    private boolean IsTableHead(String buffer) {
        return buffer != null && buffer.startsWith(AbstractDataTable.TableName);
    }

    public AbstractDataTable[] ReadTables() throws Exception {
        String line;
        ArrayList<DefaultDataTable> tables = new ArrayList();
        while ((line = bufReader.readLine()) != null) {
            if (this.IsTableHead(line)) {
                //读取表名
                String tname = line.substring(line.indexOf(":") + 1);
                //读取列名
                String[] names = bufReader.readLine().split(":");
                //读取行列数
                String[] colxrow = bufReader.readLine().split(":");
                int row = Integer.parseInt(colxrow[1]);
                //创建空表
                DefaultDataTable table = DefaultDataTable.createTable(tname, names);
                for(int i = 0; i < row; i++){
                    table.AddRow(bufReader.readLine().split(":"));
                }
                tables.add(table);
            }
        }

        return tables.toArray(new DefaultDataTable[0]);
    }
}
