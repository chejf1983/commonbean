/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.tool.convert;

/**
 *
 * @author Administrator
 */
public class MyConvertException extends Exception {

    public MyConvertException(Exception ex) {
        super(ex);
    }

    public MyConvertException(String ex) {
        super(ex);
    }

}
