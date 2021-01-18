/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.exl2;

/**
 *
 * @author chejf
 */
public class XlsConfig {

    public static String Table_Split = ":";
    public static int MaxExcelLen = 10000;//最大长度
    public static int MaxExcelWidth = 50;//最大宽度
    public static int def_column = 1;//起始列
    public static int def_row = 1;//起始行
    public static int min_row_inc = 1;
    public static int max_row_inc = 5;
    public static int min_colum_inc = 1;
    public static int max_colum_inc = 5;
    
    //|:name:column:row:|column1|column2|column3|columnn|
    //|       1         | value1| value1| value1| value1|
    //|       2         | value1| value1| value1| value1|
    //                       ...
    //|       n         | value1| value1| value1| value1|
}
