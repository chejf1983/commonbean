/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.event;

/**
 *
 * @author jiche
 */
public interface EventListener<E> {
    public abstract void recevieEvent(Event<E> event);
}
