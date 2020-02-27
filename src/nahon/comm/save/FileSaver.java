/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.save;

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
public class FileSaver implements Closeable {

    FileOutputStream out;
    OutputStreamWriter outWriter;
    BufferedWriter bufWrite;

    private FileSaver(File savefile) throws Exception {
        out = new FileOutputStream(savefile);
        outWriter = new OutputStreamWriter(out, "UTF-8");
        bufWrite = new BufferedWriter(outWriter);
    }

    public static FileSaver OpenFile(String filename) throws Exception {
        File file = new File(filename);
        return new FileSaver(file);
    }

    public void SaveTable(AbstractDataTable table) throws Exception {
        if (table == null) {
            return;
        }
        
        bufWrite.write(AbstractDataTable.TableName + ":" + table.getTableName() + "\r\n");
        for (int i = 0; i < table.getColumnCount(); i++) {
            bufWrite.write(table.getColumnName(i) + ":");
        }
        bufWrite.write("\r\n");
        bufWrite.write(table.getColumnCount() + ":" + table.getRowCount() + "\r\n");
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                bufWrite.write(table.getValueAt(i, j) + ":");
            }
            bufWrite.write("\r\n");
        }
    }

    @Override
    public void close() throws IOException {

        bufWrite.close();
        outWriter.close();
        out.close();
    }
}
