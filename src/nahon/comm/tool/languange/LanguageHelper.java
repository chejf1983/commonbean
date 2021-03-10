/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.tool.languange;

import nahon.comm.event.NEventCenter;
import nahon.comm.tool.languange.LanguageNode.Language;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chejf
 */
public final class LanguageHelper {

    public NEventCenter<Language> EventCenter = new NEventCenter<>();
    private Language language = Language.English;
    private static String filename = "Language.xml";
    private XMLAdapter saver;
    private boolean isInit = false;
    private static LanguageHelper Instance = new LanguageHelper();

    public static void SetLanguangeFile(String filepath) {
        if (!filepath.endsWith("/")) {
            filepath += "/";
        }
        filename = filepath + filename;
    }

    public static LanguageHelper getIntance() {
        if (!Instance.isInit) {
            Instance.InitLanguange();
        }
        return Instance;
    }

    private LanguageHelper() {
    }

    private void InitLanguange() {
        saver = new XMLAdapter(filename);
        String tmp = this.GetText(this.getClass().getName());

        /* Init Language, if is not exist, GetText will add one automate */
        try {
            this.language = Language.valueOf(tmp);
        } catch (Exception ex) {
            this.SetLanguage(Language.English);
        } finally {
            isInit = true;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="查找语言"> 
    public String GetText(String name) {
        LanguageNode node = new LanguageNode(name);
        if (!saver.GetNode(node)) {
            saver.AddNode(node);
            try {
                saver.Save();
            } catch (IOException ex) {
                Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
            }
        }
        return node.GetText(this.language);
    }

    public void SetText(String name, Language language, String value) throws Exception {
        LanguageNode node = new LanguageNode(name);
        if (!saver.GetNode(node)) {
            saver.AddNode(node);
        }
        node.SetText(language, value);
        saver.ModifyNode(node);
        saver.Save();
    }

    public LanguageNode FindLanguageNode(String name) throws Exception {
        LanguageNode node = new LanguageNode(name);
        if (saver.GetNode(node)) {
            return node;
        }

        return null;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="语言切换"> 
    public synchronized void SetLanguage(Language lan) {
        if (this.GetLanguage() != lan) {
            LanguageNode languageNode = new LanguageNode(this.getClass().getName());
            languageNode.SetText(Language.English, lan.toString());

            saver.ModifyNode(languageNode);
            this.language = lan;
            try {
                saver.Save();
            } catch (Exception ex) {
                Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
            }
        }

        this.EventCenter.CreateEventAsync(lan);
    }

    public void Update() {
        this.EventCenter.CreateEvent(this.language);
    }

    public Language GetLanguage() {
        return this.language;
    }
    // </editor-fold> 
}
