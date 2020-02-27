/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.tool.languange;

/**
 * SaveAble interface is used for saver adapter. MainKey is used to
 * identify the data in data base.If a class is implements this interface, 
 * there are two requests:
 * 1. It must have an unique MainKey to identify itself with other data
 * 2. all public declare which will be stored through reflect. And these 
 * declare must be String Type or int.for other types are not supported yet.
 * @author chejf
 */
public interface SaveAble {    
    abstract public String MainKey();     
    abstract public void SetMainKey(String key);
}
