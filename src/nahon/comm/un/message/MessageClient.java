package nahon.comm.un.message;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jiche
 */
public class MessageClient {

    private final ExecutorService receverThread;
    public final String ServiceID;
    private HashMap<Integer, Action> actions = new HashMap();

    public MessageClient(ExecutorService receverThread, String ServiceID) {
        this.receverThread = receverThread;
        this.ServiceID = ServiceID;
    }

    Future Receiver(final Message msg) {
        return this.receverThread.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                Action action = actions.get(msg.ID);
                if (action == null) {
                    throw new Exception("no receiver register, can't execute cmd:." + msg.ID);
                }

                if (!action.IsEnable()) {
                    throw new Exception("cmd not enabled.");
                }
                return action.ExcuteCMD(msg.args);
            }
        });
    }

    public Future SenderMessage(String target, int CMD, Object... args) throws MessageException {
        Message cmd = new Message(target, CMD, args);
        cmd.SetSender(this.ServiceID);
        return MessageService.SendMessage(cmd);
    }

    public void RegisterAction(int cmd, Action action) throws MessageException {
        if (this.actions.containsKey(cmd)) {
            throw new MessageException("AlreadyExsited CMD");
        }
        this.actions.put(cmd, action);
    }

    public void ReplaceAction(int cmd, Action action) {
        this.actions.remove(cmd);
        this.actions.put(cmd, action);
    }
    
    public void ClearAction(){
        this.actions.clear();
    }

    public void EnableAction(int cmd, boolean value) {
        Action action = this.actions.get(cmd);
        if (action != null) {
            action.EnableAction(value);
        }
    }

    public void EnableAllActions(boolean value) {
        java.util.Iterator it = this.actions.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
            ((Action) entry.getValue()).EnableAction(value);
        }
    }
}
