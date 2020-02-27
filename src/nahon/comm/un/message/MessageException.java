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
public class MessageException extends Exception {

    public MessageException(String ex) {

        super(ex);

    }

    public MessageException(Exception ex) {
        super(ex);
    }
}
