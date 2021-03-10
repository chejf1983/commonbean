/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.event;

/**
 *
 * @author jiche
 */
public class NEvent<E> {

    private final E event;
    private final Object info;

    public NEvent(E event, Object info) {
        this.event = event;
        this.info = info;
    }

    public NEvent(E event) {
        this.event = event;
        this.info = "";
    }

    public E GetEvent() {
        return this.event;
    }

    public Object Info() {
        return this.info;
    }
}
