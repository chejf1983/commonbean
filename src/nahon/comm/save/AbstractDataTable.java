/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nahon.comm.save;

/**
 *
 * @author jiche
 */
public interface AbstractDataTable {
    public static String TableName = "NTable";
    
    public String getTableName();

    public String getColumnName(int column);
    
    public int getColumnCount();

    public int getRowCount();

    public Object getValueAt(int row, int column);    
}
