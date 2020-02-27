/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.faultsystem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import nahon.comm.event.EventCenter;
import nahon.comm.event.EventListener;

/**
 *
 * @author Administrator
 */
public class LogCenter {

    private static LogCenter instance;

    private LogCenter() {
    }

    public static LogCenter Instance() {
        if (instance == null) {
            instance = new LogCenter();
        }

        return instance;
    }

    // <editor-fold defaultstate="collapsed" desc="设置LOG信息"> 
    public static String SyslogFile = ".log";//系统日志文件名称
    private String def_path = "./log";
    private int maxfilenum = 50;
    private int log_num = 0;
    private int log_max_num = 5000;

    public void SetLogPath(String filepath) {
        this.def_path = filepath;
    }

    private void CleanOldLog(String filepath) {
        File dir = new File(filepath);
        //如果文件夹不存在，创建文件夹
        if (!dir.exists()) {
            try {
                dir.mkdir();
                dir = new File(filepath);
            } catch (Exception ex) {
            }
        }

        //查看log下旧的文件个数，超过最大值，就清除旧的文件
        int morefile = dir.listFiles().length - maxfilenum;

        for (File f : dir.listFiles()) {
            if (morefile >= 0) {
                morefile--;
                f.delete();
            } else {
                break;
            }
        }
    }

    private void CreateNewLog(String filepath) {
        try {
            if (!filepath.endsWith("/")) {
                filepath += "/";
            }
            SyslogFile = filepath + new SimpleDateFormat("[yyyy_MM_dd HH_mm_ss]").format(new Date()) + SyslogFile;
            //修改默认LOG输出
            FileHandler fileHandler = new FileHandler(SyslogFile, 1024 * 1024 * 2, 1);//(2m)
            fileHandler.setFormatter(new SimpleFormatter());
            Logger.getGlobal().addHandler(fileHandler);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void CheckLog() {
        if (log_num == 0) {
            this.CleanOldLog(def_path);
            this.CreateNewLog(def_path);
        }
        
        if(log_num ++ >= log_max_num){
            log_num = 0;
        }
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="报错"> 
    private final EventCenter<Level> FaultEventCenter = new EventCenter();

    public void SendFaultReport(Level level, String info) {
        PrintLog(level, info);
        FaultEventCenter.CreateEvent(level, info);
    }

    public void SendFaultReport(Level level, Exception ex) {
        LogCenter.this.PrintLog(level, ex);
        FaultEventCenter.CreateEvent(level, ex.getMessage());
    }

    public void SendFaultReport(Level level, String info, Exception ex) {
        LogCenter.this.PrintLog(level, info, ex);
        FaultEventCenter.CreateEvent(level, info + ex.getMessage());
    }

    public void RegisterFaultEvent(EventListener<Level> list) {
        this.FaultEventCenter.RegeditListener(list);
    }

    public void UnRegisterFaultEvent(EventListener<Level> list) {
        this.FaultEventCenter.RemoveListenner(list);
    }

    public void ShowMessBox(Level level, String message) {
        FaultEventCenter.CreateEvent(level, message);
    }
    // </editor-fold>        

    // <editor-fold defaultstate="collapsed" desc="打印LOG"> 
    private final EventCenter<Level> MessageBoxCenter = new EventCenter();

    public void PrintLog(Level level, Exception ex) {
        CheckLog();
        Logger.getGlobal().log(level, null, ex);
        MessageBoxCenter.CreateEvent(level, ex.getMessage());
    }

    public void PrintLog(Level level, String info, Exception ex) {
        CheckLog();
        Logger.getGlobal().log(level, null, ex);
        MessageBoxCenter.CreateEvent(level, info + ex.getMessage());
    }

    public void PrintLog(Level level, String msg) {
        CheckLog();
        Logger.getGlobal().log(level, msg);
        MessageBoxCenter.CreateEvent(level, msg);
    }

    public void PrintLog(Level level, String msg, Object params[]) {
        CheckLog();
        Logger.getGlobal().log(level, msg, params);
        MessageBoxCenter.CreateEvent(level, msg);
    }

    public void RegisterLogEvent(EventListener<Level> list) {
        this.MessageBoxCenter.RegeditListener(list);
    }

    public void UnRegisterLogEvent(EventListener<Level> list) {
        this.MessageBoxCenter.RemoveListenner(list);
    }
    // </editor-fold>  
}
