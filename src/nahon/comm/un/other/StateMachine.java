/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.un.other;

import nahon.comm.event.NEventCenter;
import java.util.HashMap;
import nahon.comm.event.NEventListener;

/**
 *
 * @author Administrator
 * @param <T>
 */
public class StateMachine<T> {

    private NEventCenter<T> StateChange = new NEventCenter();
    private T CurrentState;
    private final HashMap<T, T[]> policy = new HashMap();

    public StateMachine(T defaultState) {
        this.CurrentState = defaultState;
    }

    public T GetCurrentState() {
        return this.CurrentState;
    }

    public synchronized boolean ChangeState(T nextstate, Object info){
        //no change policy, change directly
        if (!policy.containsKey(this.CurrentState)) {
            this.CurrentState = nextstate;
            this.StateChange.CreateEvent(nextstate, info);
            return true;
        }

        //mach policy, change directly
        for (T policystate : policy.get(this.CurrentState)) {
            if (policystate == nextstate) {
                this.CurrentState = nextstate;
                this.StateChange.CreateEvent(nextstate, info);
                return true;
            }
        }
        return false;

    }
    public boolean ChangeState(T nextstate) {
        return this.ChangeState(nextstate, null);
    }

    public void AddPolicy(T state, T... nextState) {
        if (this.policy.containsKey(state)) {
            this.policy.remove(state);
        }

        this.policy.put(state, nextState);
    }
    
    public void RegisterStateChangeListener(NEventListener<T> list){
        this.StateChange.RegeditListener(list);
    }
    
    public void Clean(){
        this.StateChange.RemoveAllListenner();
    }
    
    public void RemoveChangeListener(NEventListener<T> list){
        this.StateChange.RemoveListenner(list);
    }
}
