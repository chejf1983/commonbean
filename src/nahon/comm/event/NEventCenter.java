/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.event;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import nahon.comm.faultsystem.LogCenter;

/**
 *
 * @author chejf
 */
public class NEventCenter<E> {

    private final ArrayList<NEventListener> listeners = new ArrayList<>();
//    private ExecutorService process;
    private final Lock elock = new ReentrantLock();

    public int GetListenersNum() {
        return this.listeners.size();
    }

    // <editor-fold defaultstate="collapsed" desc="Listener 管理"> 
    public void RegeditListener(NEventListener<E> list) {
        if (list != null) {
            this.listeners.add(list);
            if(this.listeners.size() > 10){
                LogCenter.Instance().PrintLog(Level.SEVERE, "监听人数超过10个:" + listeners.size());
            }
        }
    }

    public void RemoveListenner(NEventListener<E> list) {
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
            sendEvent(new NEvent(eventType, eventInfo));
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
//        elock.lock();
//        try {
//            if (this.process == null) {
//                process = Executors.newSingleThreadExecutor();
//            }
//        } finally {
//            elock.unlock();
//        }

        new Thread((new AsyncEvent(new NEvent(eventType, eventInfo)))).start();
    }

    private class AsyncEvent implements Runnable {

        NEvent event = null;

        AsyncEvent(NEvent event) {
            this.event = event;
        }

        @Override
        public void run() {
            sendEvent(this.event);
        }
    }
    // </editor-fold> 

    private synchronized void sendEvent(NEvent<E> event) {
        while (listeners.contains(null)) {
            listeners.remove(null);
        }

        for(NEventListener tmp : this.listeners){
            try {
                tmp.recevieEvent(event);
            } catch (Exception ex) {
                Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
            }
        };
    }
}
