package nahon.comm.un.message;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
public class MessageService {
    public static int messageThreadPoolNum = 20;
    private static ExecutorService receverThread = null;
    private static HashMap<String, MessageClient> list = new HashMap();

    public static void UnregisterCMDService(MessageClient instance){
        MessageService.list.remove(instance.ServiceID);
    }
            
    public static MessageClient CreateMessageClient(String ID) {
        if (MessageService.receverThread == null) {
            MessageService.InitCMDSystem();
        }
        
        MessageClient newClient = new MessageClient(MessageService.receverThread, ID);
        MessageService.list.put(ID, newClient);
        
        return newClient;
    }
        
    public static void DeleteMessageClient(String ID){
        MessageService.list.get(ID).ClearAction();
        MessageService.list.remove(ID);
    }
    
    public static Future SendMessage(Message cmd) throws MessageException{
        MessageClient target = MessageService.list.get(cmd.target);
        if(target == null){
            throw new MessageException("Incorrect Target");
        }
        
        return target.Receiver(cmd);
    }

    public static void InitCMDSystem() {
        MessageService.receverThread = Executors.newCachedThreadPool();
    }

    public static void ShutDownCMDSystem() {
        MessageService.list.clear();
        MessageService.receverThread.shutdownNow();
        MessageService.receverThread = null;
    }
}
