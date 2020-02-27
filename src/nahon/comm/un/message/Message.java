/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nahon.comm.un.message;

/**
 *
 * @author jiche
 */
public class Message{
    public final int ID;
    public String sender = "0";
    public String target = "0";
    public Object[] args;
    public Message(String target, int ID, Object... args){
        this.target = target;
        this.ID = ID;
        this.args = args;
    }
    
    public void SetSender(String sender){
        this.sender = sender;
    }
}
