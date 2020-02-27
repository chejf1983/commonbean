package nahon.comm.un.message;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jiche
 */
public abstract class Action {

    private boolean isEnable = true;

    public abstract Object ExcuteCMD(Object... args) throws Exception;

    public void EnableAction(boolean value) {
        this.isEnable = value;
    }

    public boolean IsEnable() {
        return this.isEnable;
    }
}
