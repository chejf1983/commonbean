/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.tool.languange;


/**
 *
 * @author chejf
 */
public class LanguageNode implements SaveAble{  
    private String listname;
    
    // <editor-fold defaultstate="collapsed" desc="Store datas Area">
    public String englishtext;
    public String chinesetext;   
    public String japanesetext;
    // </editor-fold> 
    
    @Override
    public String MainKey() {
        return this.listname;
    }

    @Override
    public void SetMainKey(String key) {
        this.listname = key;
    }
    
    public LanguageNode(String nodeName){
        this.listname = nodeName;
        this.englishtext = nodeName;
        this.chinesetext = nodeName;
        this.japanesetext = nodeName;
    }
    
    public enum Language{
        English,
        Chinese,
        Japanese
    }
    
    public String GetText(Language language){
        switch(language){
            case English:
                return this.englishtext;
            case Chinese:
                return this.chinesetext;
            case Japanese:
                return this.japanesetext;
            default:
                return this.chinesetext;
        }
    }
    
    public void SetText(Language language, String value){
        switch(language){
            case English:
                this.englishtext = value;
                break;
            case Chinese:
                this.chinesetext = value;
                break;
            case Japanese:
                this.japanesetext = value;
                break;
            default:
                this.chinesetext = value;
        }
    }
}
