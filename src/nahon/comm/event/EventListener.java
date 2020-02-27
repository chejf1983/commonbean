/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.event;

/**
 *
 * @author jiche
 */
public abstract class EventListener<E> {
    public abstract void recevieEvent(Event<E> event);
    
    public boolean IsEnable(){
        return true;
    }
}
