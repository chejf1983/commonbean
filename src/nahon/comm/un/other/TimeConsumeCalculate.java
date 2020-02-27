/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.un.other;

/**
 *
 * @author jiche
 */
public class TimeConsumeCalculate {

    private long lasttime_ns;
    private long consumetime_ns;
    private boolean isEnable = true;
    private String tag = "";
    public static TimeConsumeCalculate intsnce = new TimeConsumeCalculate();
//    public EventCenter<Double> TimeConsumeEvent = new EventCenter<>();

    private TimeConsumeCalculate() {
    }

    public void SetTag(String tag) {
        this.tag = tag;
    }

    public void SetTimeFlag() {
        this.lasttime_ns = System.currentTimeMillis();
    }

    public void PrintTime() {
        this.PrintTime("");
    }

    public void PrintTime(String info) {
        long tmp = System.currentTimeMillis();
        this.consumetime_ns = tmp - this.lasttime_ns;
        this.lasttime_ns = tmp;
        System.out.println(tag + info + consumetime_ns + " ms" );
    }
}
