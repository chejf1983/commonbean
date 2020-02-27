/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.event;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chejf
 */
public class EventCenter<E> {

    private final ArrayList<EventListener> listeners = new ArrayList<>();
    private ExecutorService process;
    private final Lock elock = new ReentrantLock();

    public int GetListenersNum() {
        return this.listeners.size();
    }

    // <editor-fold defaultstate="collapsed" desc="Listener 管理"> 
    public void RegeditListener(EventListener<E> list) {
        if (list != null) {
            this.listeners.add(list);
        }
    }

    public void RemoveListenner(EventListener<E> list) {
        this.listeners.remove(list);
    }

    public void RemoveAllListenner() {
        this.listeners.clear();
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="同步步通知接口"> 
    public synchronized void CreateEvent(E eventType, Object eventInfo) {
        elock.lock();
        try {
            sendEvent(new Event(eventType, eventInfo));
        } finally {
            elock.unlock();
        }
    }

    public synchronized void CreateEvent(E eventType) {
        elock.lock();
        try {
            this.CreateEvent(eventType, null);
        } finally {
            elock.unlock();
        }
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="异步通知接口">  
    //异步通知事件
    public void CreateEventAsync(E eventType) {
        this.CreateEventAsync(eventType, null);
    }

    public void CreateEventAsync(E eventType, Object eventInfo) {
        elock.lock();
        try {
            if (this.process == null) {
                process = Executors.newSingleThreadExecutor();
            }
        } finally {
            elock.unlock();
        }

        process.submit(new AsyncEvent(new Event(eventType, eventInfo)));
    }

    private class AsyncEvent implements Runnable {

        Event event = null;

        AsyncEvent(Event event) {
            this.event = event;
        }

        @Override
        public void run() {
            sendEvent(this.event);
        }
    }
    // </editor-fold> 

    private synchronized void sendEvent(Event<E> event) {
        while (listeners.contains(null)) {
            listeners.remove(null);
        }

        for (EventListener tmp : this.listeners) {
            if (!tmp.IsEnable()) {
                this.listeners.remove(tmp);
                break;
            }
        }

        this.listeners.forEach((tmp) -> {
            try {
                tmp.recevieEvent(event);
            } catch (Exception ex) {
                Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
            }
        });
    }
}
